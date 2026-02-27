package cz.crcs.ectester.standalone.libs;

import java.util.Set;

public class PKCS11Lib extends GenericPKCS11Library {

    public PKCS11Lib() {
        super("SunPKCS11Provider", System.getenv("PIN") != null,
                System.getenv("PKCS11_CFG"),
                System.getenv("PIN"));
    }

    @Override
    public Set<String> getCurves() {
        return Set.of();
    }
}
