Renesas RZ/Five SMARC
=====================

[Renesas RZ/Five SMARC](https://www.renesas.com/eu/en/products/microcontrollers-microprocessors/rz-mpus/rzfive-evaluation-board-kit-rzfive-evaluation-board-kit) The RZ/Five evaluation kit (EVK) is ideal for evaluating and developing with the Renesas RZ/Five MPU. The EVK includes the RZ/Five SMARC v2.1 System on Module (SoM) and its carrier board. The module board complies with the SMARC v2.1 standard. This document will cover how to build SD card image and flash the bootloader images to RZ/Five SMARC EVK.

How to build
============

```
$ SHELL=/bin/bash kas-container build ./meta-riscv/smarc-rzfive.yml
```

The `kas-container` script can be installed from
[kas repository](https://github.com/siemens/kas.git).

Build results
=============

- Flash_Writer_SCIF_RZFIVE_SMARC.mot
- spl-smarc-rzfive.srec
- fit-smarc-rzfive.srec
- core-image-minimal-smarc-rzfive.rootfs.wic.gz
- core-image-minimal-smarc-rzfive.rootfs.tar.bz2
- Image-smarc-rzfive.bin
- r9a07g043f01-smarc.dtb


Flashing SD card
================

```
$ gunzip -c core-image-minimal-smarc-rzfive.rootfs.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress && sync
```
Alternatively you can use https://etcher.balena.io/ tool to flash the wic.gz image to SD card.

The Linux image (Image-smarc-rzfive.bin) and the device tree
(r9a07g043f01-smarc.dtb) are stored in the first partition.
The second partition holds the root filesystem.

Flashing U-boot
===============

1. Booting Flash Writer

Turn off the power and set the SW11 dipswitches to `off-on-off-on`.

```
    +-+-+-+-+
 ON | |X| |X|
OFF |X| |X| | SW11
    +-+-+-+-+
```

Turn on the power and you will see the messages below:

```
SCIF Download mode
 (C) Renesas Electronics Corp.
-- Load Program to System RAM ---------------
please send !
```

From your host PC, open a terminal and run the following command
(Assume that `/dev/ttyUSB0` is the serial port connecting to RZ/Five SMARC EVK):

```
$ cat Flash_Writer_SCIF_RZFIVE_SMARC.mot | sudo tee /dev/ttyUSB0
```
Alternatively you can use Tera Term. Open a “Send file” dialog by selecting “File” → “Sendfile” menu

After successfully downloading the binary, Flash Writer starts automatically and shows a message like below on the terminal.

```
Flash writer for RZ/Five Series V1.02 Nov.15,2021 Product Code : RZ/Five
>
```

2. Writing U-Boot SPL

Enter `XLS2`, `11E00` and `00000` in the Flash Writer prompt.

```
>XLS2
===== Qspi writing of RZ/Five Board Command =============
Load Program to Spiflash
Writes to any of SPI address.
 Winbond : W25Q128JW
Program Top Address & Qspi Save Address
===== Please Input Program Top Address ============
  Please Input : H'11E00

===== Please Input Qspi Save Address ===
  Please Input : H'00000
Work RAM(H'50000000-H'53FFFFFF) Clear....
please send ! ('.' & CR stop load)
```

In another terminal, run the following command:

```
$ cat spl-smarc-rzfive.srec | sudo tee /dev/ttyUSB0
```

When the transfer is complete, enter `y` to update SPI flash.

3. Writing U-Boot ITB

The U-Boot ITB (fit-smarc-rzfive.srec) includes OpenSBI and
U-Boot proper.

Enter `XLS2`, `00000` and `20000` in the Flash Writer prompt.

```
>XLS2
===== Qspi writing of RZ/Five Board Command =============
Load Program to Spiflash
Writes to any of SPI address.
 Winbond : W25Q128JW
Program Top Address & Qspi Save Address
===== Please Input Program Top Address ============
  Please Input : H'00000

===== Please Input Qspi Save Address ===
  Please Input : H'20000
Work RAM(H'50000000-H'53FFFFFF) Clear....
please send ! ('.' & CR stop load)
```

In another terminal, run the following command:

```
$ cat smarc-rzfive.srec | sudo tee /dev/ttyUSB0
```

When the transfer is complete, enter `y` to update SPI flash.

After the U-Boot binaries are flashed, turn off the power and set the
SW11 dipswitches to `off-off-off-on`.

```
    +-+-+-+-+
 ON | | | |X|
OFF |X|X|X| | SW11
    +-+-+-+-+
```

Next, power on the board and you should see the updated U-Boot prompt.

You may need to reset the environment variables:

```
=> env default -a
=> saveenv
```

Power on the board again and the U-Boot should automatically boot Linux from the SD card.

Resources
=========

* [Renesas RZ/Five SMARC EVK](https://www.renesas.com/eu/en/products/microcontrollers-microprocessors/rz-mpus/rzfive-evaluation-board-kit-rzfive-evaluation-board-kit)
* [Renesas Info](https://renesas.info/wiki/RZ-Five)
* [Renesas RZ/Five General-purpose Microprocessors with RISC-V CPU Core (Andes AX45MP Single) (1.0 GHz) with 2ch Gigabit Ethernet](https://www.renesas.com/us/en/products/microcontrollers-microprocessors/rz-mpus/rzfive-general-purpose-microprocessors-risc-v-cpu-core-andes-ax45mp-single-10-ghz-2ch-gigabit-ethernet)
