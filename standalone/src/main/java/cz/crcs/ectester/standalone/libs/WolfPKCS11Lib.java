package cz.crcs.ectester.standalone.libs;

import java.util.Set;

public class WolfPKCS11Lib extends GenericPKCS11Library {

    public WolfPKCS11Lib() {
        super("wolfPKCS11", "pkcs11-resources/wolfPKCS11.cfg");
    }

    @Override
    public Set<String> getCurves() {
        return Set.of();
    }
}
