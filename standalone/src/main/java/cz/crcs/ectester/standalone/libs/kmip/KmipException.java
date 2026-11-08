package cz.crcs.ectester.standalone.libs.kmip;

public class KmipException extends RuntimeException {
    public KmipException(String message) {
        super(message);
    }

    public KmipException(String message, Throwable cause) {
        super(message, cause);
    }
}
