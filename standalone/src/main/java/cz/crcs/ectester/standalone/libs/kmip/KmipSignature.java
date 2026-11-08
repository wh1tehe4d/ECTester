package cz.crcs.ectester.standalone.libs.kmip;

import java.nio.ByteBuffer;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.cyphera.kmip.KmipClient;
import io.cyphera.kmip.Operations;

public abstract class KmipSignature extends SignatureSpi {

    private final KmipClient client;

    private KmipKey key;

    private final List<Byte> buffer;

    public enum DigestType {
        SELF_DIGEST, LIB_DIGEST, NO_DIGEST
    }

    public enum KmipSignatureType {
        ECDSA, NONEwithECDSA, SHA224withECDSA, SHA256withECDSA, SHA384withECDSA, SHA512withECDSA,

        // we do the hashing with these
        SHA3_224withECDSA, SHA3_256withECDSA, SHA3_384withECDSA, SHA3_512withECDSA;

        public DigestType getDigestType() {
            return switch (this) {
                case SHA3_224withECDSA, SHA3_256withECDSA, SHA3_384withECDSA, SHA3_512withECDSA -> DigestType.SELF_DIGEST;
                case ECDSA, SHA224withECDSA, SHA256withECDSA, SHA384withECDSA, SHA512withECDSA -> DigestType.LIB_DIGEST;
                case NONEwithECDSA -> DigestType.NO_DIGEST;
            };
        }
    }

    private final KmipSignatureType type;

    private final MessageDigest digest;

    private State state = State.UNINITIALIZED;

    private enum State {
        UNINITIALIZED, SIGN, VERIFY
    }

    public KmipSignature(KmipClient client, KmipSignatureType type) {
        super();
        this.client = client;
        this.type = type;
        try {
            if (this.type.getDigestType() == DigestType.SELF_DIGEST) {
                this.digest = MessageDigest.getInstance("SHA3-" + this.type.toString().substring(5, 8));
            } else {
                this.digest = null;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new KmipException("Error when creating KMIP signature.", e);
        }

        this.buffer = this.digest == null ? new ArrayList<>() : null;
    }

    /*private Set<KmipSignatureType> supportedSignatureTypes() {
        this.client.query();
    }*/


    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof KmipKey))
            throw new InvalidKeyException("KMIP requires an instance of KmipKey key.");

        KmipKey key = (KmipKey) publicKey;

        if (key.isPrivate())
            throw new InvalidKeyException("KMIP verifying requires an instance of KmipKey private key.");

        this.engineInitVerify(key);
    }

    private void engineInitVerify(KmipKey publicKey) {
        assert !publicKey.isPrivate();
        this.key = publicKey;
        this.state = State.VERIFY;
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof KmipKey))
            throw new InvalidKeyException("KMIP requires an instance of KmipKey key.");

        KmipKey key = (KmipKey) privateKey;

        if (!key.isPrivate())
            throw new InvalidKeyException("KMIP signing requires an instance of KmipKey private key.");

        this.engineInitSign(key);
    }

    private void engineInitSign(KmipKey privateKey) {
        assert privateKey.isPrivate();
        this.key = privateKey;
        this.state = State.SIGN;
    }

    private void validateInit(State expected) throws SignatureException {
        if (this.state == State.UNINITIALIZED && expected == State.SIGN)
            throw new SignatureException("Signature needs to be initialized before signing.");

        if (this.state == State.UNINITIALIZED && expected == State.VERIFY)
            throw new SignatureException("Signature needs to be initialized before verifying.");

        if (this.state == State.VERIFY && expected == State.SIGN)
            throw new SignatureException("Signature cannot be initialized for verifying before signing.");

        if (this.state == State.SIGN && expected == State.VERIFY)
            throw new SignatureException("Signature cannot be initialized for signing before verifying.");
    }

    /*@Override
    protected void engineUpdate(byte b) throws SignatureException {
        if (this.type.isSelfDigesting()) {
            try {
                this.digest.update(b);
            } catch (Exception e) {
                throw new SignatureException("Error while updating signature.", e);
            }
        } else {
            this.buffer.add(b);
        }



    }*/

    /*@Override
    protected void engineUpdate(byte[] bytes, int i, int i1) throws SignatureException {
        if (this.type == KmipSignatureType.NONEwithECDSA) {
            if (this.offset + i1 >= this.buffer.length)
                throw new SignatureException("Raw ECDSA does not support signing of this long data.");

            if (i + i1 >= bytes.length)
                throw new SignatureException("Input array shorter than expected.");

            for (; i < i + i1; i++) {
                this.buffer[offset++] = bytes[i];
            }
            return;
        }

        this.digest.update(bytes, i, i1);
    }*/

    @Override
    protected void engineUpdate(ByteBuffer input) {
        throw new KmipException("Unimplemented method.");
    }

    /*@Override
    protected byte[] engineSign() throws SignatureException {
        validateInit(State.SIGN);
        Operations.SignResult result;
        try {
            if (this.type == KmipSignatureType.ECDSA)
                result = this.client.sign(this.key.getUniqueId(), this.buffer);
            else
                result = this.client.sign(this.key.getUniqueId(), this.digest.digest());
        } catch (Exception e) {
            throw new SignatureException("Error while signing data.", e);
        }

        if (result.signatureData == null)
            throw new SignatureException("Error while signing data.");

        return result.signatureData;
    }

    @Override
    protected int engineSign(byte[] outbuf, int offset, int len) throws SignatureException {
        if (len < outbuf.length - offset)
            throw new SignatureException("Insufficient output buffer size.");

        byte[] signature = this.engineSign();
        int i;
        for (i = 0; i < (Math.min(signature.length, len)); ++i) {
            outbuf[offset++] = signature[i];
        }

        return i;
    }

    @Override
    protected boolean engineVerify(byte[] bytes) throws SignatureException {
        validateInit(State.VERIFY);

    }*/
}
