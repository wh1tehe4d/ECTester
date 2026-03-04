package cz.crcs.ectester.standalone.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class PKCS11ConfigWriter {

    private static final String defaultPath = "pkcs11-resources/tmp_pkcs11.cfg";

    public static boolean write(PKCS11Config config) {
        return PKCS11ConfigWriter.write(config, PKCS11ConfigWriter.defaultPath);
    }

    public static boolean write(PKCS11Config config, String path) {
        try (FileWriter fw = new FileWriter(path)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(config.export());
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
}
