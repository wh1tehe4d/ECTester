package cz.crcs.ectester.standalone.libs;

import java.util.Set;

public class PKCS11Lib extends GenericPKCS11Library {

    public PKCS11Lib() {
        this("",
                System.getenv("PKCS11_CFG"),
                System.getenv("PIN"));
    }

    public PKCS11Lib(String name, String pkcs11ConfigPath, String pin) {
        super((name.isEmpty() ? "" : name + " ") +"(PKCS11 implementation)", pin != null, pkcs11ConfigPath, pin);
    }

    @Override
    public Set<String> getCurves() {
        return Set.of();
    }
}
