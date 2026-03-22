# ECTester PKCS#11 manual
## 1. Arbitrary PKCS#11 implementation usage
* --pkcs11 < implementationPath;implementationName >
<br> Specify the path to .so and the implementation name. If own configuration is provided, the path is ignored.
* --pkcs11-login < PIN >
<br> Specify the user PIN for logging into an implementation. Required for some crypto operations.
If not provided, ECTester assumes the implementation does not require logging in.
* --pkcs11-cfg < configPath >
<br> Specify an own SunPKCS11 config. If not provided, ECTester tries to create a default configuration file based on previous experiences.
## 2. Supported PKCS#11 implementations usage
* Usage similiar to other SW libraries supported by ECTester with few specialities specified bellow
* Compiled shared objects are part of the tool, located in standalone/src/main/resources/cz/crcs/ectester/standalone/libs/pkcs11/
* Assumes ECTESTER_HOME is set to the root directory of ECTester
* java -jar $ECTESTER_HOME/standalone/build/libs/ECTesterStandalone.jar <options> [one of the following lib names]
### 2.1 SoftHSMv2-OPENSSL
* Compiled using OpenSSL backend
* Pre-configured token
* Assumes PIN is set to 1234
* Assumes SOFTHSM2_CONF is set to $ECTESTER_HOME/standalone/src/main/resources/cz/crcs/ectester/standalone/libs/pkcs11/SoftHSMv2/SoftHSMv2-OPENSSL/softhsm2-openssl.conf
### 2.2 SoftHSMv2-BOTAN
* Compiled using Botan backend
* Pre-configured token
* Assumes PIN is set to 1234
* Assumes SOFTHSM2_CONF is set to $ECTESTER_HOME/standalone/src/main/resources/cz/crcs/ectester/standalone/libs/pkcs11/SoftHSMv2/SoftHSMv2-BOTAN/softhsm2-botan.conf
* Botan backend SEG faults after our testing and creates an error log (even after being just initialized, therefore is commented out from the libs in source code, to prevent the SEG fault after just listing libs)
* NOTE: it SEG faults even when called using its C api, therefore problem is probably in the library itself (issue is double free of memory on cleanup)
### 2.3 wolfPKCS11
* Compiled using WolfSSL backend
* Pre-configured token, no PIN required
* Assumes WOLFPKCS11_TOKEN_PATH is set to $ECTESTER_HOME/standalone/src/main/resources/cz/crcs/ectester/standalone/libs/pkcs11/wolfPKCS11/token
* has library-side hardcoded limit of 64 objects, therefore after some testing it starts crashing because it is not able to generate any new keys
* Solution to this is to delete contents of $WOLFPKCS11_TOKEN_PATH and reinit the token using pkcs11-tool