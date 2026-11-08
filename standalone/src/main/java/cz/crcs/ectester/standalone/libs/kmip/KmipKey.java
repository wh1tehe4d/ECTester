package cz.crcs.ectester.standalone.libs.kmip;

import io.cyphera.kmip.Tag;
import org.bouncycastle.asn1.sec.ECPrivateKey;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KmipKey implements PublicKey, PrivateKey {

    private final String uniqueId;

    private final int algorithm;

    private final boolean privateKey;

    public KmipKey(String uniqueId, int algorithm, boolean isPrivate) {
        this.uniqueId = uniqueId;
        this.algorithm = algorithm;
        this.privateKey = isPrivate;
    }

    public static KmipKey newPrivateKey(String uniqueId, int algorithm) {
        return new KmipKey(uniqueId, algorithm, true);
    }

    public static KmipKey newPublicKey(String uniqueId, int algorithm) {
        return new KmipKey(uniqueId, algorithm, false);
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    public boolean isPrivate() {
        return this.privateKey;
    }

    @Override
    public String getAlgorithm() {
        return switch (this.algorithm) {
            case Tag.ALG_ECDSA -> "ECDSA";
            default -> "UNKNOWN";
        };
    }

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public byte[] getEncoded() {
        return new byte[0];
    }
}
