package cz.crcs.ectester.standalone.libs.kmip.client;


import io.cyphera.kmip.KmipClient;
import io.cyphera.kmip.Operations;
import io.cyphera.kmip.Tag;
import io.cyphera.kmip.Ttlv;

public class KmipClientExtended {

    private final KmipClient client;

    public KmipClientExtended() {
        this.client = null;
        this.client.sign()
    }

    public Operations.SignResult sign(String uniqueId, byte[] data, byte[] digestedData,
                                      byte[] correlationValue, boolean initValue, boolean finalValue) {
        byte[] request = Ttlv.encodeStructure(Tag.REQUEST_PAYLOAD,
                Ttlv.encodeTextString(Tag.UNIQUE_IDENTIFIER, uniqueId),
                Ttlv.encodeByteString(Tag.DATA, data),
                Ttlv.encodeByteString(Tag.)
                )
    }
}
