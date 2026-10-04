package cz.crcs.ectester.standalone.libs;

import cz.crcs.ectester.common.util.FileUtil;
import cz.crcs.ectester.standalone.util.PKCS11Config;
import cz.crcs.ectester.standalone.util.PKCS11ConfigWriter;
import cz.crcs.ectester.standalone.util.PKCS11Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class WolfPKCS11Lib extends GenericPKCS11Library {

    public final Path tokenDir;

    public WolfPKCS11Lib() {
        super("wolfPKCS11");
        this.tokenDir = FileUtil.getAppData()
                .resolve("ECTesterStandalone")
                .resolve("ECTester-wolfpkcs11-token");
    }

    public static String getResource() {
        return PKCS11Util.getAbsoluteResourcePath(WolfPKCS11Lib.resource());
    }

    private static String resource() {
        return Paths.get("wolfPKCS11", "libwolfpkcs11." + FileUtil.getLibSuffix()).toString();
    }

    /*private boolean initToken() {
        try {
            this.tokenDir = Files.createTempDirectory("ECTester-wolfpkcs11-token");
            this.tokenDir.toFile().deleteOnExit();

            this.symLink = Files.createSymbolicLink(FileUtil.getAppData()
                    .resolve("ECTesterStandalone")
                    .resolve("ECTester-wolfpkcs11-token"), tokenDir);
            symLink.toFile().deleteOnExit();

            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }*/

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
        return success && /*this.initToken() &&*/ super.initialize();
    }

    @Override
    public void destroy() throws IOException {
        for (File object : Objects.requireNonNull(this.tokenDir.toFile().listFiles())) {
            if (!object.delete()) {
                throw new IOException("Cannot delete contents of the generated token for wolfPKCS11");
            }
        }

        if (!this.tokenDir.toFile().delete()) {
            throw new IOException("Cannot delete contents of the generated token for wolfPKCS11");
        }
    }
}
