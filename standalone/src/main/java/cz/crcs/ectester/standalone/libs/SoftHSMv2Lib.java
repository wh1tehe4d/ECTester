package cz.crcs.ectester.standalone.libs;

import cz.crcs.ectester.standalone.util.PKCS11Config;
import cz.crcs.ectester.standalone.util.PKCS11ConfigWriter;
import cz.crcs.ectester.standalone.util.PKCS11Util;

/**
 * Class representing the SoftHSMv2 library.
 *
 * @author Filip Horvath
 */
public abstract class SoftHSMv2Lib extends GenericPKCS11Library {

    private final Backend backend;

    public SoftHSMv2Lib(Backend backend) {
        super("SoftHSMv2-" + backend, "1234");
        this.backend = backend;
    }

    @Override
    public boolean initialize() {
        boolean success = PKCS11ConfigWriter.write(PKCS11Config.SoftHSMv2Config(this.backend));
        this.setProviderConfigPath(PKCS11ConfigWriter.getConfigPath());
        return success && super.initialize();
    }

    public static String getResource(Backend backend) {
        return PKCS11Util.getResource("SOFTHSM2_LIB",
                String.format("SoftHSMv2/SoftHSMv2-%s/libsofthsm2.so", backend));
    }

    public enum Backend {
        OPENSSL,
        BOTAN
    }

}
