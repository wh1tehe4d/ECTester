package cz.crcs.ectester.standalone.libs;

import cz.crcs.ectester.standalone.util.PKCS11Config;
import cz.crcs.ectester.standalone.util.PKCS11ConfigWriter;

import java.util.Set;

public class WolfPKCS11Lib extends GenericPKCS11Library {

    public WolfPKCS11Lib() {
        super("wolfPKCS11", "standalone/src/main/resources/cz/crcs/ectester/standalone/libs/pkcs11/wolfPKCS11/wolfPKCS11.cfg");
    }

    @Override
    public boolean initialize() {
        PKCS11ConfigWriter.write(PKCS11Config.wolfPKCS11Config(), this.providerConfigPath);
        return super.initialize();
    }

    @Override
    public Set<String> getCurves() {
        return Set.of();
    }
}
