package cz.crcs.ectester.standalone.libs;

import java.util.Set;

/**
 * Class representing the SoftHSMv2 library.
 *
 * @author Filip Horvath
 */
public class SoftHSMv2Lib extends GenericPKCS11Library {

    private final SoftHSMBackend backend;

    public SoftHSMv2Lib() {
        this(SoftHSMBackend.OPENSSL);
    }

    public SoftHSMv2Lib(SoftHSMBackend backend) {
        super("SoftHSMv2",
                "pkcs11-resources/SoftHSMv2.cfg",
                System.getenv("PIN"));
        this.backend = backend;
    }

    @Override
    public Set<String> getCurves() {
        // TODO add the curves based on lib documentation
        return switch(this.backend) {
            case OPENSSL -> Set.of();
            case BOTAN -> Set.of();
        };
    }

    public enum SoftHSMBackend {
        OPENSSL,
        BOTAN
    }

}
