    package cz.crcs.ectester.standalone.libs;

import java.security.KeyStore;
import java.security.Security;
import java.util.Set;

/**
 * Class for representing a generic PKCS11 implementation, configured by a HW or a SW PKCS11 module.
 *
 * @author Filip Horvath
 */
public abstract class GenericPKCS11Library extends ProviderECLibrary {


    private char[] PIN;

    public GenericPKCS11Library(String name, String pkcs11ConfigPath) {
        this(name, pkcs11ConfigPath, null);
    }

    public GenericPKCS11Library(String name, String pkcs11ConfigPath, String PIN) {
        super(name, Security
                .getProvider("SunPKCS11")
                .configure(pkcs11ConfigPath));

        if (PIN != null) {
            this.PIN = PIN.toCharArray();
        }
    }

    @Override
    public boolean initialize() {
        boolean initialized = super.initialize();
        if (!initialized || this.PIN == null) return initialized;

        try {
            KeyStore ks = KeyStore.getInstance("PKCS11", this.provider);
            ks.load(null, this.PIN);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            initialized = false;
        }

        return initialized;
    }
}
