#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static const char* attributes = "\n\nattributes(generate, CKO_PRIVATE_KEY, *) = {\n  CKA_DERIVE = true\n  CKA_SIGN = true\n  CKA_SENSITIVE = false\n  CKA_EXTRACTABLE = true\n}\n\nattributes(generate, CKO_SECRET_KEY, *) = {\n  CKA_EXTRACTABLE = true\n  CKA_SENSITIVE = false\n}";

void usage(char* this) {
    printf("PKCS11 interface runner for ECTester.\n");
    printf("\nUsage:\n");
    printf("%s  [ -h | --help ] [ -d | --debug <loggerPath> <logPath> ]\n[ -l | --login <PIN> ] [ -L | --lib <libName> <libPath> ]\n[ -e | --ectester-home <ECTesterHomePath> ] [ -a | --args <ECTesterArguments> ]\n", this);
}

int main(int argc, void** argv) {
    if (argc <= 1) {
        usage((char *) argv[0]);
        return 1;
    }

    char* loggerPath = NULL;
    char* logPath = NULL;
    char* PIN = NULL;
    char* libName = NULL;
    char* libPath = NULL;
    char* home = ".";
    char* args = "test default";
    
    for (int i = 1; i < argc; i++) {
        char *curr = (char *) argv[i];
        if (strcmp(curr, "-h") == 0 || strcmp(curr, "--help") == 0) {
            usage((char *) argv[0]);
            return 0;
        } else if (strcmp(curr, "-d") == 0 || strcmp(curr, "--debug") == 0) {
            if (argc <= i + 2) {
                printf("--debug or -d option must be followed by <loggerPath> and <logPath>.\n");
                return 1;
            }

            loggerPath = (char *) argv[i + 1];
            
            logPath = (char *) argv[i + 2];
            i += 2;
        } else if (strcmp(curr, "-l") == 0 || strcmp(curr, "--login") == 0) {
            if (argc <= i + 1) {
                printf("--login option must be followed by the <PIN>.\n");
                return 1;
            }

            PIN = (char *) argv[i + 1];
            setenv("PIN", PIN, 1);
            i++;
        } else if (strcmp(curr, "-L") == 0 || strcmp(curr, "--lib") == 0) {
            if (argc <= i + 2) {
                printf("-L or --lib option must be followed by <libName> and <libPath>.\n");
                return 1;
            }

            libName = (char *) argv[i + 1];
            libPath = (char *) argv[i + 2];
            i += 2;
        } else if (strcmp(curr, "-e") == 0 || strcmp(curr, "--ectester-home") == 0) {
            if (argc <= i + 1) {
                printf("-e or --ectester-home option must be followed by <ECTesterHomePath>.\n");
                return 1;
            }

            home = (char *) argv[i + 1];
            i++;
        } else if (strcmp(curr, "-a") == 0 || strcmp(curr, "--args") == 0) {
            if (argc <= i + 1) {
                printf("-a or --args option must be followed by <ECTesterArguments>.\n");
                return 1;
            }

            args = (char *) argv[i + 1];
            ++i;
        } else {
            printf("Unknown option: %s.\nUse %s -h or %s --help for hints on usage.\n", (char *) argv[i], (char *) argv[0], (char *) argv[0]);
            return 1;
        }
    }

    if (libPath == NULL) {
        printf("Fatal error. PKCS11 library path must be specified using the -L or --lib option followed by <libName> and <libPath>.\n");
        return 1;
    }

    if (loggerPath != NULL) {
        setenv("PKCS11_LOGGER_LIBRARY_PATH", libPath, 1);
        libPath = loggerPath;
        setenv("PKCS11_LOGGER_LOG_FILE_PATH", logPath, 1);
    }

    size_t n = strlen(home);
    char* path = (char *) malloc(n + 16);
    if (path == NULL) return 1;
    memcpy(path, home, n);
    memcpy(path + n, "/tmp_pkcs11.cfg", 16);
    FILE* pkcs11_cfg = fopen(path, "w");
    setenv("PKCS11_CFG", path, 1);
    free(path);
    if (pkcs11_cfg == NULL) return 1;

    n = strlen(libName);
    size_t n2 = strlen(libPath);
    char* c = (char *) malloc(n + n2 + 19);
    if (c == NULL) {
        fclose(pkcs11_cfg);
        return 1;
    }

    memcpy(c, "name = ", 7);
    memcpy(c + 7, libName, n);
    memcpy(c + 7 + n, "\n\nlibrary = ", 12);
    memcpy(c + 19 + n, libPath, n2);
    fwrite(c, n + n2 + 19, 1ul, pkcs11_cfg);
    free(c);

    fwrite(attributes, 231, 1ul, pkcs11_cfg);
    fclose(pkcs11_cfg);

    n = strlen(args);
    n2 = strlen(home);
    c = (char *) malloc(64 + n + n2);
    memcpy(c, "java -jar ", 10);
    memcpy(c + 10, home, n2);
    memcpy(c + 10 + n2, "/standalone/build/libs/ECTesterStandalone.jar ", 46);
    memcpy(c + 56 + n2, args, n);
    memcpy(c + 56 + n + n2, " pkcs11", 8);
    //printf("%s\n", getenv("PIN"));
    //printf("%s\n", getenv("PKCS11_CFG"));
    system(c);
    


    //system("rm tmp_pkcs11.cfg");
    c[0] = 'r';
    c[1] = 'm';
    c[2] = ' ';
    memcpy(c + 3, home, n2);
    memcpy(c + 3 + n2, "/tmp_pkcs11.cfg", 16);
    system(c);
    free(c);

    return 0;
}