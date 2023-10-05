FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:smarc-rzfive = "\
    file://0001-ld-emulparams-elf32lriscv-defs.sh-Adjust-TEXT_START_.patch \
"

