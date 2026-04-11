package cz.crcs.ectester.standalone.libs;

import cz.crcs.ectester.common.util.FileUtil;
import cz.crcs.ectester.standalone.util.PKCS11Config;
import cz.crcs.ectester.standalone.util.PKCS11ConfigWriter;
import cz.crcs.ectester.standalone.util.PKCS11Util;

public class WolfPKCS11Lib extends GenericPKCS11Library {

    public WolfPKCS11Lib() {
        super("wolfPKCS11");
    }

    public static String getResource() {
        return PKCS11Util.getAbsoluteResourcePath(WolfPKCS11Lib.resource());
    }

    private static String resource() {
        return "wolfPKCS11/libwolfpkcs11." + FileUtil.getLibSuffix();
    }

    @Override
    public boolean initialize() {
        PKCS11Config config;
        try {
            config = PKCS11Config.wolfPKCS11Config();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        }

        boolean success = PKCS11ConfigWriter.write(config);
        this.setProviderConfigPath(PKCS11ConfigWriter.getConfigPath());
        return success && super.initialize();
    }
}
