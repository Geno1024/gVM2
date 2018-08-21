package com.geno1024.gvm.chipset

import com.geno1024.gvm.devices.Motherboard
import com.geno1024.gvm.hex16
import com.geno1024.gvm.hex2

class CPU constructor(val DEBUG: Boolean = false)
{
    lateinit var motherboard: Motherboard
    val memory by lazy { motherboard.memory }

    var XA: Long = 0x0000_0000_0000_0000
    var XB: Long = 0x0000_0000_0000_0000
    var XC: Long = 0x0000_0000_0000_0000
    var XD: Long = 0x0000_0000_0000_0000
    var XE: Long = 0x0000_0000_0000_0000
    var XF: Long = 0x0000_0000_0000_0000
    var XG: Long = 0x0000_0000_0000_0000
    var XH: Long = 0x0000_0000_0000_0000

    var CS: Long = 0x0000_0000_0000_0000
    var IP: Long = 0x0000_0000_0000_0000

    /*
                        /------------------------------------
                        |/-----------------------------------
                        ||/----------------------------------
                        |||/---------------------------------
                        ||||/--------------------------------
                        |||||/-------------------------------
                        ||||||/------------------------------
                        |||||||/-----------------------------
                        |||||||| /---------------------------
                        |||||||| |/--------------------------
                        |||||||| ||/-------------------------
                        |||||||| |||/------------------------
                        |||||||| ||||/-----------------------
                        |||||||| |||||/----------------------
                        |||||||| ||||||/---------------------
                        |||||||| |||||||/--------------------
                        |||||||| ||||||||  /-----------------
                        |||||||| ||||||||  |/----------------
                        |||||||| ||||||||  ||/---------------
                        |||||||| ||||||||  |||/--------------
                        |||||||| ||||||||  ||||/------------- 
                        |||||||| ||||||||  |||||/------------ 
                        |||||||| ||||||||  ||||||/-----------
                        |||||||| ||||||||  |||||||/----------
                        |||||||| ||||||||  |||||||| /--------
                        |||||||| ||||||||  |||||||| |/-------
                        |||||||| ||||||||  |||||||| ||/------
                        |||||||| ||||||||  |||||||| |||/-----
                        |||||||| ||||||||  |||||||| ||||/----
                        |||||||| ||||||||  |||||||| |||||/---
                        |||||||| ||||||||  |||||||| ||||||/--
                        |||||||| ||||||||  |||||||| |||||||/-
                        |||||||| ||||||||  |||||||| ||||||||  */
    var FLAGS: Long = 0b00000000_00000000__00000000_00000000__00000000_00000000__00000000_00000000
    /*                                                        |||||||| ||||||||  |||||||| ||||||||
                                                             -/||||||| ||||||||  |||||||| ||||||||
                                                             --/|||||| ||||||||  |||||||| ||||||||
                                                             ---/||||| ||||||||  |||||||| ||||||||
                                                             ----/|||| ||||||||  |||||||| ||||||||
                                                             -----/||| ||||||||  |||||||| ||||||||
                                                             ------/|| ||||||||  |||||||| ||||||||
                                                             -------/| ||||||||  |||||||| ||||||||
                                                             --------/ ||||||||  |||||||| ||||||||
                                                             ----------/|||||||  |||||||| ||||||||
                                                             -----------/||||||  |||||||| ||||||||
                                                             ------------/|||||  |||||||| ||||||||
                                                             -------------/||||  |||||||| ||||||||
                                                             --------------/|||  |||||||| ||||||||
                                                             ---------------/||  |||||||| ||||||||
                                                             ----------------/|  |||||||| ||||||||
                                                             -----------------/  |||||||| ||||||||
                                                             --------------------/||||||| ||||||||
                                                             ---------------------/|||||| ||||||||
                                                             ----------------------/||||| ||||||||
                                                             -----------------------/|||| ||||||||
                                                             ------------------------/||| ||||||||
                                                             -------------------------/|| ||||||||
                                                             --------------------------/| ||||||||
                                                             ---------------------------/ ||||||||
                                                             -----------------------------/|||||||
                                                             ------------------------------/||||||
                                                             -------------------------------/|||||
                                                             --------------------------------/||||
                                                             ---------------------------------/|||
    CF: Carry Flag                                           ----------------------------------/||
    ZF: Zero Flag                                            -----------------------------------/|
    PF: Parity Flag                                          ------------------------------------/
                                                             */

    val CF = 1L shl 2
    val ZF = 1L shl 1
    val PF = 1L shl 0

    fun run(ip: Long = IP)
    {
        IP = ip
        while (true)
        {
            val left = memory.read1(IP).toInt() and 0xff
            val right = memory.read1(IP + 1).toInt() and 0xff
            if (DEBUG) println("$this: ${left.hex2()}${right.hex2()}\n")

            // left nibble
            mapOf(
                0x00 to l00,
                0x01 to l01,
                0x02 to l02,
                0xf0 to lf0
            )[left]?.invoke(right)?:err()
        }
    }

    val l00 = { i: Int ->
        mapOf(
            0x00 to { IP += 2 },
            0x01 to { System.exit(XA.toInt()) }
        )[i]?.invoke()?:err()
    }

    val l01 = { i: Int ->
        mapOf(
            0x00 to { XA++; IP += 2; },
            0x01 to { XB++; IP += 2; },
            0x02 to { XC++; IP += 2; },
            0x03 to { XD++; IP += 2; },
            0x04 to { XE++; IP += 2; },
            0x05 to { XF++; IP += 2; },
            0x06 to { XG++; IP += 2; },
            0x07 to { XH++; IP += 2; },

            0x40 to { XA += memory.read8(IP + 2); IP += 10; },
            0x41 to { XB += memory.read8(IP + 2); IP += 10; },
            0x42 to { XC += memory.read8(IP + 2); IP += 10; },
            0x43 to { XD += memory.read8(IP + 2); IP += 10; },
            0x44 to { XE += memory.read8(IP + 2); IP += 10; },
            0x45 to { XF += memory.read8(IP + 2); IP += 10; },
            0x46 to { XG += memory.read8(IP + 2); IP += 10; },
            0x47 to { XH += memory.read8(IP + 2); IP += 10; },

            0x50 to { XA += memory.read4(IP + 2); IP += 6; },
            0x51 to { XB += memory.read4(IP + 2); IP += 6; },
            0x52 to { XC += memory.read4(IP + 2); IP += 6; },
            0x53 to { XD += memory.read4(IP + 2); IP += 6; },
            0x54 to { XE += memory.read4(IP + 2); IP += 6; },
            0x55 to { XF += memory.read4(IP + 2); IP += 6; },
            0x56 to { XG += memory.read4(IP + 2); IP += 6; },
            0x57 to { XH += memory.read4(IP + 2); IP += 6; },

            0x70 to { XA += memory.read1(IP + 2); IP += 3; },
            0x71 to { XB += memory.read1(IP + 2); IP += 3; },
            0x72 to { XC += memory.read1(IP + 2); IP += 3; },
            0x73 to { XD += memory.read1(IP + 2); IP += 3; },
            0x74 to { XE += memory.read1(IP + 2); IP += 3; },
            0x75 to { XF += memory.read1(IP + 2); IP += 3; },
            0x76 to { XG += memory.read1(IP + 2); IP += 3; },
            0x77 to { XH += memory.read1(IP + 2); IP += 3; },

            0x80 to { XA--; IP += 2; },
            0x81 to { XB--; IP += 2; },
            0x82 to { XC--; IP += 2; },
            0x83 to { XD--; IP += 2; },
            0x84 to { XE--; IP += 2; },
            0x85 to { XF--; IP += 2; },
            0x86 to { XG--; IP += 2; },
            0x87 to { XH--; IP += 2; },

            0xC0 to { XA -= memory.read8(IP + 2); IP += 10; },
            0xC1 to { XB -= memory.read8(IP + 2); IP += 10; },
            0xC2 to { XC -= memory.read8(IP + 2); IP += 10; },
            0xC3 to { XD -= memory.read8(IP + 2); IP += 10; },
            0xC4 to { XE -= memory.read8(IP + 2); IP += 10; },
            0xC5 to { XF -= memory.read8(IP + 2); IP += 10; },
            0xC6 to { XG -= memory.read8(IP + 2); IP += 10; },
            0xC7 to { XH -= memory.read8(IP + 2); IP += 10; },

            0xD0 to { XA -= memory.read4(IP + 2); IP += 6; },
            0xD1 to { XB -= memory.read4(IP + 2); IP += 6; },
            0xD2 to { XC -= memory.read4(IP + 2); IP += 6; },
            0xD3 to { XD -= memory.read4(IP + 2); IP += 6; },
            0xD4 to { XE -= memory.read4(IP + 2); IP += 6; },
            0xD5 to { XF -= memory.read4(IP + 2); IP += 6; },
            0xD6 to { XG -= memory.read4(IP + 2); IP += 6; },
            0xD7 to { XH -= memory.read4(IP + 2); IP += 6; },

            0xF0 to { XA -= memory.read1(IP + 2); IP += 3; },
            0xF1 to { XB -= memory.read1(IP + 2); IP += 3; },
            0xF2 to { XC -= memory.read1(IP + 2); IP += 3; },
            0xF3 to { XD -= memory.read1(IP + 2); IP += 3; },
            0xF4 to { XE -= memory.read1(IP + 2); IP += 3; },
            0xF5 to { XF -= memory.read1(IP + 2); IP += 3; },
            0xF6 to { XG -= memory.read1(IP + 2); IP += 3; },
            0xF7 to { XH -= memory.read1(IP + 2); IP += 3; }
        )[i]?.invoke()?:err()
    }

    val l02 = { i: Int ->
        mapOf(
            0x00 to { XA = memory.read8(IP + 2); IP += 10; },
            0x01 to { XB = memory.read8(IP + 2); IP += 10; },
            0x02 to { XC = memory.read8(IP + 2); IP += 10; },
            0x03 to { XD = memory.read8(IP + 2); IP += 10; },
            0x04 to { XE = memory.read8(IP + 2); IP += 10; },
            0x05 to { XF = memory.read8(IP + 2); IP += 10; },
            0x06 to { XG = memory.read8(IP + 2); IP += 10; },
            0x07 to { XH = memory.read8(IP + 2); IP += 10; },

            0x10 to { XA = memory.read4(IP + 2).toLong(); IP += 6; },
            0x11 to { XB = memory.read4(IP + 2).toLong(); IP += 6; },
            0x12 to { XC = memory.read4(IP + 2).toLong(); IP += 6; },
            0x13 to { XD = memory.read4(IP + 2).toLong(); IP += 6; },
            0x14 to { XE = memory.read4(IP + 2).toLong(); IP += 6; },
            0x15 to { XF = memory.read4(IP + 2).toLong(); IP += 6; },
            0x16 to { XG = memory.read4(IP + 2).toLong(); IP += 6; },
            0x17 to { XH = memory.read4(IP + 2).toLong(); IP += 6; },

            0x30 to { XA = memory.read1(IP + 2).toLong(); IP += 3; },
            0x31 to { XB = memory.read1(IP + 2).toLong(); IP += 3; },
            0x32 to { XC = memory.read1(IP + 2).toLong(); IP += 3; },
            0x33 to { XD = memory.read1(IP + 2).toLong(); IP += 3; },
            0x34 to { XE = memory.read1(IP + 2).toLong(); IP += 3; },
            0x35 to { XF = memory.read1(IP + 2).toLong(); IP += 3; },
            0x36 to { XG = memory.read1(IP + 2).toLong(); IP += 3; },
            0x37 to { XH = memory.read1(IP + 2).toLong(); IP += 3; },

            0x50 to { XA = (XA and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x51 to { XB = (XB and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x52 to { XC = (XC and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x53 to { XD = (XD and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x54 to { XE = (XE and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x55 to { XF = (XF and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x66 to { XG = (XG and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },
            0x57 to { XH = (XH and 0x7fff_ffff_0000_0000) + memory.read4(IP + 2).toLong(); IP += 6; },

            0x70 to { XA = (XA and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x71 to { XB = (XB and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x72 to { XC = (XC and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x73 to { XD = (XD and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x74 to { XE = (XE and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x75 to { XF = (XF and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x76 to { XG = (XG and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; },
            0x77 to { XH = (XH and 0x7fff_ffff_ffff_ff00) + memory.read1(IP + 2).toLong(); IP += 3; }
        )[i]?.invoke()?:err()
    }

    val lf0 = { i: Int ->
        mapOf(
            0x0f to { while (true) {  } }
        )[i]?.invoke()?:err()
    }



    fun err(): Nothing = throw Exception("Not implemented.")

    override fun toString(): String =
        "XA=${XA.hex16()} XB=${XB.hex16()} XC=${XC.hex16()} XD=${XD.hex16()}\n" +
        "XE=${XE.hex16()} XF=${XF.hex16()} XG=${XG.hex16()} XH=${XH.hex16()}\n" +
        "FLAGS=${FLAGS.toString(2).padStart(64, '0')}\n" +
        "IP=${IP.hex16()}"
}