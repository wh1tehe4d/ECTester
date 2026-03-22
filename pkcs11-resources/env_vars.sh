#!/usr/bin/env bash
export ECTESTER_HOME=/home/filip/Program/fi-muni/CROCS/ECTester
export PKCS11_LOGGER_LIBRARY_PATH=/usr/local/lib/softhsm/libsofthsm2.so
export PKCS11_LOGGER_LOG_FILE_PATH=/home/filip/Program/fi-muni/CROCS/ectester-pkcs11/logs/log
export PKCS11_LOGGER_PATH=/home/filip/Program/fi-muni/CROCS/ectester-pkcs11/pkcs11-logger/build/linux/pkcs11-logger-x64.so
export SOFTHSM2_CONF=$ECTESTER_HOME/standalone/src/main/resources/cz/crcs/ectester/standalone/libs/pkcs11/SoftHSMv2/SoftHSMv2-OPENSSL/softhsm2-openssl.conf
export PIN=1234