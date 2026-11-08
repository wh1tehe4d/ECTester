package cz.crcs.ectester.standalone.libs.kmip;

import io.cyphera.kmip.KmipClient;
import io.cyphera.kmip.Operations;
import io.cyphera.kmip.Tag;

import java.io.IOException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.Random;

public class KmipKeyPairGenerator extends KeyPairGeneratorSpi {

    private final KmipClient client;

    private ECParameterSpec params;

    // currently no effect since the client doesn't support many parameters
    private Random random;

    public KmipKeyPairGenerator(KmipClient client) {
        super();
        this.client = client;
    }

    @Override
    public void initialize(int i, SecureRandom secureRandom) {
    }

    @Override
    public KeyPair generateKeyPair() {
        Operations.CreateKeyPairResult result;

        try {
             result = client.createKeyPair("", Tag.ALG_ECDSA, 1);
        } catch (io.cyphera.kmip.KmipException | IOException e) {
            throw new KmipException("Error creating a keypair.");
        }

        return new KeyPair(KmipKey.newPublicKey(result.publicKeyUid, Tag.ALG_ECDSA),
                KmipKey.newPublicKey(result.privateKeyUid, Tag.ALG_ECDSA));
    }

    @Override
    public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        if (!(params instanceof ECParameterSpec))
            throw new InvalidAlgorithmParameterException("KMIP provider currently only supports ECC.");

        this.params = (ECParameterSpec) params;
        this.random = random;
    }
}
