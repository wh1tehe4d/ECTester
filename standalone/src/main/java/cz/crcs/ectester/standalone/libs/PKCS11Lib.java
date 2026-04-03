package cz.crcs.ectester.standalone.libs;

public class PKCS11Lib extends GenericPKCS11Library {

    public PKCS11Lib() {
        this("",
                System.getenv("PKCS11_CFG"),
                System.getenv("PIN"));
    }

    public PKCS11Lib(String name, String providerConfigPath, String pin) {
        super((name.isEmpty() ? "" : name + " ") +"(PKCS11 implementation)", pin);
        this.setProviderConfigPath(providerConfigPath);
    }
}
