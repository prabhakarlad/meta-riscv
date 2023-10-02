require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
DEPENDS:append = " dtc-native u-boot-tools-native opensbi"

SRCREV="1804c8ab17551a1442fbc123771b6ee2781ce9db"
BRANCH="smarc-rzfive"

SRC_URI = "git://github.com/prabhakarlad/u-boot.git;protocol=https;branch=${BRANCH} \
           file://BootLoaderHeader.bin \
           "

UBOOT_CONFIG_smarc-rzfive = "smarc-rzf_defconfig"

do_compile:prepend() {
    export OPENSBI=${DEPLOY_DIR_IMAGE}/fw_dynamic.bin
}

do_compile:append() {
    oe_runmake -C ${S} O=${B} ${UBOOT_CONFIG}
    oe_runmake -C ${S} O=${B} -j4
}

do_compile:append() {
    cat ${WORKDIR}/BootLoaderHeader.bin  ${B}/spl/u-boot-spl.bin > ${B}/u-boot-spl_bp.bin
    objcopy -I binary -O srec --adjust-vma=0x00011E00 --srec-forceS3 ${B}/u-boot-spl_bp.bin ${B}/spl-${MACHINE}.srec
    objcopy -I binary -O srec --adjust-vma=0 --srec-forceS3 ${B}/u-boot.itb ${B}/fit-${MACHINE}.srec
}

do_deploy:append() {
    install -m 755 ${B}/spl-${MACHINE}.srec ${DEPLOY_DIR_IMAGE}
    install -m 755 ${B}/fit-${MACHINE}.srec ${DEPLOY_DIR_IMAGE}
}

do_compile[depends] += "opensbi:do_deploy"

COMPATIBLE_MACHINE = "(smarc-rzfive)"
