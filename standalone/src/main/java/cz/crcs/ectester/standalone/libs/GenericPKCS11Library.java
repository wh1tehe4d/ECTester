package cz.crcs.ectester.standalone.libs;

import java.security.KeyStore;
import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.InvalidAlgorithmParameterException;

import java.security.spec.ECGenParameterSpec;

import java.util.Set;
import java.util.TreeSet;
import java.util.Enumeration;

import org.bouncycastle.jce.ECNamedCurveTable;

/**
 * Class for representing a generic PKCS11 implementation, configured by a HW or a SW PKCS11 module.
 *
 * @author Filip Horvath
 */
public abstract class GenericPKCS11Library extends ProviderECLibrary {

    private final char[] PIN;

    protected final String providerConfigPath;

    public GenericPKCS11Library(String name, String providerConfigPath) {
        this(name, providerConfigPath, null);
    }

    public GenericPKCS11Library(String name, String providerConfigPath, String PIN) {
        super(name, Security.getProvider("SunPKCS11"));

        // if providerConfigPath is null, try to resolve to default config
        // based on the name, taking into account the backends of SoftHSMv2 if
        // none are found raise
        if ( providerConfigPath != null ) {
            this.providerConfigPath = providerConfigPath;
        } else {
            String defaultPath = null;
            if ( name.startsWith("SoftHSMv2-") )  {
                defaultPath = String.format("cz/crcs/ectester/standalone/libs/pkcs11/SoftHSMv2/%s/%s.cfg", name, name);
            } else {
                defaultPath = String.format("cz/crcs/ectester/standalone/libs/pkcs11/%s/%s.cfg", name, name);
            }
            String resourcePath = this.getClass().getClassLoader().getResource(defaultPath).getPath();
            if ( resourcePath == null ) {
                throw new IllegalArgumentException(
                    String.format("Could not resolve path to the configuration for '%s' PKCS11 module", name)
                );
            }
            this.providerConfigPath = resourcePath;
        }
        System.out.println(this.providerConfigPath);
        this.PIN = PIN != null ? PIN.toCharArray() : null;
    }

    @Override
    public boolean initialize() {
        boolean initialized;
        try {
            // configure SunPKCS11 by a config, which provides .so
            this.provider = this.provider.configure(this.providerConfigPath);

            initialized = super.initialize();
            if (!initialized || this.PIN == null) return initialized;

            // PKCS#11 login procedure
            KeyStore ks = KeyStore.getInstance("PKCS11", this.provider);
            ks.load(null, this.PIN);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            initialized = false;
        }

        return initialized;
    }

    @Override
    public Set<String> getCurves() {
        Set<String> result = new TreeSet<>();
        Enumeration<?> names = ECNamedCurveTable.getNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            try {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", this.provider);
                kpg.initialize(new ECGenParameterSpec(name));
                result.add(name);
            } catch (InvalidAlgorithmParameterException | ProviderException | NoSuchAlgorithmException e) { }
        }
        return result;
    }
}
