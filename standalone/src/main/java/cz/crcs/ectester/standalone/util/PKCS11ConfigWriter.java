package cz.crcs.ectester.standalone.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PKCS11ConfigWriter {

    private static String configPath = null;

    public static boolean write(PKCS11Config config) {
        try {
            File tempFile = File.createTempFile("ECTester-SunPKCS11_config", ".tmp");
            tempFile.deleteOnExit();
            configPath = tempFile.getAbsolutePath();
            return write(config, configPath);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static String getConfigPath() {
        return configPath;
    }

    private static boolean write(PKCS11Config config, String path) {
        try (FileWriter fw = new FileWriter(path)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(config.export());
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            // TODO print stacktrace line by line
            System.err.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
}
