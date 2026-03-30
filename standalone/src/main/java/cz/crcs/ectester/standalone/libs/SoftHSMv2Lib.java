package cz.crcs.ectester.standalone.libs;

import java.nio.file.Path;

import cz.crcs.ectester.standalone.util.PKCS11Config;
import cz.crcs.ectester.standalone.util.PKCS11ConfigWriter;


/**
 * Class representing the SoftHSMv2 library.
 *
 * @author Filip Horvath
 */
public abstract class SoftHSMv2Lib extends GenericPKCS11Library {

    private final Backend backend;

    public SoftHSMv2Lib(Backend backend) {
        super("SoftHSMv2-" + backend, System.getenv("SOFTHSM2_CONF"), System.getenv("PIN"));
        this.backend = backend;
    }

    @Override
    public boolean initialize() {
        PKCS11ConfigWriter.write(PKCS11Config.SoftHSMv2Config(this.backend), this.providerConfigPath);
        return super.initialize();
    }

    public enum Backend {
        OPENSSL,
        BOTAN
    }

}
