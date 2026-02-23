    package cz.crcs.ectester.standalone.libs;

import java.security.KeyStore;
import java.security.Security;
import java.util.Set;

/**
 * Class for representing a PKCS11 provider, configured by a HW or a SW PKCS11 module.
 *
 * @author Filip Horvath
 */
public class PKCS11Lib extends ProviderECLibrary {

    public PKCS11Lib() {
        super("SunPKCS11", Security
                .getProvider("SunPKCS11")
                .configure(System.getenv("PKCS11_CFG")));
    }

    @Override
    public boolean initialize() {
        boolean initialized = super.initialize();
        try {
            KeyStore ks = KeyStore.getInstance("PKCS11");
            ks.load(null, new char[] {'1', '2', '3','4', '5'});
        } catch (Exception e) {
            System.err.println(e.getMessage());
            initialized = false;
        }

        return initialized;
    }

    @Override
    public Set<String> getCurves() {
        return Set.of();
    }
}
