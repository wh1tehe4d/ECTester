{
  lib,
  runtimeShell,
  writeShellApplication,

  softhsm,
  ectester,

  botan3,
  openssl,
  sqlite,

  backend ? "openssl",
}:
let
  softhsmPkg = softhsm.overrideAttrs (prev: rec {

    buildInputs = if backend == "openssl" then
      [ openssl sqlite ]
    else
      throw ''
        Unless botan3 is supported by SoftHSMv2, see https://github.com/softhsm/SoftHSMv2/issues/792
        the only supported backend is 'openssl'.
      ''
      [ botan3 sqlite ]
    ;

    sansBackendConfigureFlags = builtins.filter (
      f: !( (lib.hasPrefix "--with-crypto" f) || (lib.hasPrefix "--with-openssl" f))
    ) prev.configureFlags;

    configureFlags = if backend == "openssl" then prev.configureFlags
    else
    sansBackendConfigureFlags ++ [
      "--with-crypto-backend=botan"
      "--with-botan=${lib.getDev botan3}/include/botan-3/botan"
    ];

  });
in
writeShellApplication {
  name = "ECTesterStandalone";

  runtimeInputs = [
    softhsmPkg
  ];

  text = ''
    SOFTHSM2_TEMPDIR=$(mktemp --directory)
    SOFTHSM2_CONF="$SOFTHSM2_TEMPDIR"/softhsm2.conf
    SOFTHSM2_LIB="${softhsmPkg}/lib/softhsm/libsofthsm2.so"
    PIN=1234

    export SOFTHSM2_CONF SOFTHSM2_LIB PIN

    cat <<EOF > "$SOFTHSM2_TEMPDIR"/softhsm2.conf
    directories.tokendir = $SOFTHSM2_TEMPDIR/tokens
    objectstore.backend = file
    objectstore.umask = 0077

    # ERROR, WARNING, INFO, DEBUG
    log.level = ERROR

    # If CKF_REMOVABLE_DEVICE flag should be set
    slots.removable = false

    # Enable and disable PKCS#11 mechanisms using slots.mechanisms.
    slots.mechanisms = ALL

    # If the library should reset the state on fork
    library.reset_on_fork = false
    EOF

    mkdir --parents "$SOFTHSM2_TEMPDIR"/tokens
    softhsm2-util --init-token --slot 0 --label "ECTester" --pin "$PIN" --so-pin "$PIN" > /dev/null

    ${ectester.outPath}/bin/ECTesterStandalone "$@"
    rm --recursive --force "$SOFTHSM2_TEMPDIR"
  '';
}
