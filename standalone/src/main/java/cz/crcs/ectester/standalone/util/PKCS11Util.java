package cz.crcs.ectester.standalone.util;

import cz.crcs.ectester.standalone.ECTesterStandalone;

public class PKCS11Util {

    public static final String PKCS11_RESOURCES_PATH = ECTesterStandalone.class.getResource("").getPath()
            .split("build/libs/ECTesterStandalone.jar!")[0].substring(5)
            + "build/resources/main/cz/crcs/ectester/standalone/libs/pkcs11/";

    public static String getResource(String nixVar, String defaultPath) {
        String resource = System.getenv(nixVar);
        if (resource != null) {
            // nix set the variable for us, we are being run through nix
            return resource;
        }

        // this resolves the resources from where we are being run from, and we append the specific .so path
        return PKCS11Util.PKCS11_RESOURCES_PATH + defaultPath;
    }
}
