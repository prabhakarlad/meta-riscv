# require recipes-kernel/linux/linux-renesas.inc
require linux-mainline-common.inc
FILESEXTRAPATHS:prepend = "${FILE_DIRNAME}/linux-smarc-rzfive:"
SUMMARY = "Renesas RZ/Five SMARC EVK kernel recipe"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "rzfive-upstream"
SRCREV="bf7c6b12b5c8bdde9254d1d648ac99ccd65b2b46"
FORK ?= "prabhakarlad"

SRC_URI = "git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
           file://set-mmap-min-addr.cfg \
           "

LINUX_VERSION ?= "6.6.0"
LINUX_VERSION_EXTENSION:append:smarc-rzfive = "-rzfive"

KBUILD_DEFCONFIG = "rzfive_defconfig"

COMPATIBLE_MACHINE = "(smarc-rzfive)"
