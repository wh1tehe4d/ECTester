package cz.crcs.ectester.standalone.util;

import java.util.*;
import java.util.stream.Collectors;

public class PKCS11Config {

    private final String name;

    private final String implementationPath;

    private final Map<CKO, Map<CKA, Boolean>> values;

    private PKCS11Config(String name, String implementationPath, Map<CKO, Map<CKA, Boolean>> values) {
        this.name = name;
        this.implementationPath = implementationPath;
        this.values = values.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, (ckoMapEntry -> new HashMap<>(ckoMapEntry.getValue()))));
    }

    public static PKCS11Config defaultConfig(String name, String implementationPath) {
        return PKCS11Config.builder()
                .name(name)
                .implementationPath(implementationPath)
                .attribute(CKO.GENERATE_CKO_PRIVATE_KEY, CKA.CKA_DERIVE, true)
                .attribute(CKO.GENERATE_CKO_PRIVATE_KEY, CKA.CKA_SIGN, true)
                .attribute(CKO.GENERATE_CKO_PRIVATE_KEY, CKA.CKA_EXTRACTABLE, true)
                .attribute(CKO.GENERATE_CKO_PRIVATE_KEY, CKA.CKA_SENSITIVE, false)
                .attribute(CKO.GENERATE_CKO_PRIVATE_KEY, CKA.CKA_TOKEN, true)
                .attribute(CKO.GENERATE_CKO_PUBLIC_KEY, CKA.CKA_DERIVE, true)
                .attribute(CKO.GENERATE_CKO_PUBLIC_KEY, CKA.CKA_VERIFY, true)
                .attribute(CKO.GENERATE_CKO_PUBLIC_KEY, CKA.CKA_TOKEN, true)
                .attribute(CKO.GENERATE_CKO_SECRET_KEY, CKA.CKA_EXTRACTABLE, true)
                .attribute(CKO.GENERATE_CKO_SECRET_KEY, CKA.CKA_SENSITIVE, false)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String export() {
        StringBuilder result = new StringBuilder();
        result.append("name = ").append(this.name).append(System.lineSeparator());
        result.append("library = ").append(this.implementationPath).append(System.lineSeparator());
        for (Map.Entry<CKO, Map<CKA, Boolean>> entry : this.values.entrySet()) {
            result.append(entry.getKey().header());
            for (Map.Entry<CKA, Boolean> att : entry.getValue().entrySet()) {
                result.append(String.format("  %s = %s" + System.lineSeparator(), att.getKey(), att.getValue()));
            }
            result.append("}" + System.lineSeparator());
        }

        return result.toString();
    }

    public enum CKO {
        GENERATE_CKO_PUBLIC_KEY,
        GENERATE_CKO_PRIVATE_KEY,
        GENERATE_CKO_SECRET_KEY,
        GENERATE_ANY_KEY,
        IMPORT_CKO_PUBLIC_KEY,
        IMPORT_CKO_PRIVATE_KEY,
        IMPORT_CKO_SECRET_KEY,
        IMPORT_ANY_KEY;

        private String header() {
            String value = switch(this) {
                case GENERATE_ANY_KEY -> "generate, *";
                case GENERATE_CKO_PUBLIC_KEY -> "generate, CKO_PUBLIC_KEY";
                case GENERATE_CKO_PRIVATE_KEY -> "generate, CKO_PRIVATE_KEY";
                case GENERATE_CKO_SECRET_KEY -> "generate, CKO_SECRET_KEY";
                case IMPORT_ANY_KEY -> "import, *";
                case IMPORT_CKO_PUBLIC_KEY -> "import, CKO_PUBLIC_KEY";
                case IMPORT_CKO_PRIVATE_KEY -> "import, CKO_PRIVATE_KEY";
                case IMPORT_CKO_SECRET_KEY -> "import, CKO_SECRET_KEY";
            };
            return String.format("attributes(%s, *) = {" + System.lineSeparator(), value);
        }
    }

    public enum CKA {
        CKA_PRIVATE,
        CKA_SENSITIVE,
        CKA_EXTRACTABLE,
        CKA_DERIVE,
        CKA_SIGN,
        CKA_VERIFY,
        CKA_TOKEN
    }

    public static class Builder {

        private String name;

        private String implementationPath;

        private Map<CKO, Map<CKA, Boolean>> values = new HashMap<>();

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder implementationPath(String implementationPath) {
            this.implementationPath = implementationPath;
            return this;
        }

        public Builder attribute(CKO cko, CKA cka, boolean value) {
            Map<CKA, Boolean> values = this.values.getOrDefault(cko, new HashMap<>());
            values.put(cka, value);
            this.values.putIfAbsent(cko, values);
            return this;
        }

        public PKCS11Config build() {
            if (this.name == null || this.implementationPath == null) {
                throw new IllegalArgumentException("Both name and implementationPath must be set.");
            }

            return new PKCS11Config(this.name, this.implementationPath, this.values);
        }

    }
}
