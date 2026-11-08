package cz.crcs.ectester.standalone.libs.kmip;

import io.cyphera.kmip.KmipClient;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.List;
import java.util.Map;

public class KMIPProvider extends Provider {

    private KmipClient client;

    protected KMIPProvider(String name, String versionStr, String info) {
        super(name, versionStr, info);
        putService(new Service(this, "Signature", "NONEwithECDSA", "KmipSignature", null, null));
        putService(new Service(this, "Signature", "ECDSA", "KmipSignature", null, null));
    }

    public KmipClient getClient() {
        return this.client;
    }

    @Override
    public Provider configure(String configArg) {
        this.client = new KmipClient.Builder()
                .host("")
                .port(0)
                .clientCert("")
                .clientKey("")
                .caCert("")
                .build();

        return super.configure(configArg);
    }

    @Override
    public boolean isConfigured() {
        return this.client != null;
    }

    private static final class Service extends Provider.Service {
        public Service(Provider provider, String type, String algorithm, String className, List<String> aliases, Map<String, String> attributes) {
            super(provider, type, algorithm, className, aliases, attributes);
        }

        @Override
        public Object newInstance(Object constructorParameter) throws NoSuchAlgorithmException {
            String type = this.getType();
            String algo = this.getAlgorithm();

            if (!type.equals("Signature"))
                throw new NoSuchAlgorithmException("KMIP does not currently support " + type + ".");

            KmipSignature.KmipSignatureType sigType;
            try {
                 sigType = KmipSignature.KmipSignatureType.valueOf(algo);
            } catch (NullPointerException | IllegalArgumentException e) {
                throw new NoSuchAlgorithmException("This algorithm is currently not supported.", e);
            }
            return null;
            //return new KmipSignature(((KMIPProvider) this.getProvider()).getClient(), sigType);
        }
    }
}
