package cz.crcs.ectester.standalone.libs;

import java.util.Set;

/**
 * Class representing the SoftHSMv2 library.
 *
 * @author Filip Horvath
 */
public class SoftHSMv2Lib extends GenericPKCS11Library {

    private SoftHSMBackend backend = SoftHSMBackend.OPENSSL;

    public SoftHSMv2Lib() {
        super("SoftHSMv2",
                true,
                "pkcs11-resources/SoftHSMv2.cfg",
                System.getenv("PIN"));
    }

    public SoftHSMv2Lib(SoftHSMBackend backend) {
        this();
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
