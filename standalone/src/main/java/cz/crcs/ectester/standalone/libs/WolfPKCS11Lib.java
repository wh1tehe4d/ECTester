package cz.crcs.ectester.standalone.libs;

import cz.crcs.ectester.standalone.util.PKCS11Config;
import cz.crcs.ectester.standalone.util.PKCS11ConfigWriter;
import cz.crcs.ectester.standalone.util.PKCS11Util;

public class WolfPKCS11Lib extends GenericPKCS11Library {

    public WolfPKCS11Lib() {
        super("wolfPKCS11");
    }

    public static String getResource() {
        return PKCS11Util.getResource("WOLFPKCS11_LIB", "wolfPKCS11/libwolfpkcs11.so");
    }

    @Override
    public boolean initialize() {
        PKCS11ConfigWriter.write(PKCS11Config.wolfPKCS11Config());
        this.setProviderConfigPath(PKCS11ConfigWriter.getConfigPath());
        return super.initialize();
    }
}
