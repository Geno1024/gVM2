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

            0x10 to { XA--; IP += 2; },
            0x11 to { XB--; IP += 2; },
            0x12 to { XC--; IP += 2; },
            0x13 to { XD--; IP += 2; },
            0x14 to { XE--; IP += 2; },
            0x15 to { XF--; IP += 2; },
            0x16 to { XG--; IP += 2; },
            0x17 to { XH--; IP += 2; },

            0x40 to { XA += memory.read16(IP + 2); IP += 18; },
            0x41 to { XB += memory.read16(IP + 2); IP += 18; },
            0x42 to { XC += memory.read16(IP + 2); IP += 18; },
            0x43 to { XD += memory.read16(IP + 2); IP += 18; },
            0x44 to { XE += memory.read16(IP + 2); IP += 18; },
            0x45 to { XF += memory.read16(IP + 2); IP += 18; },
            0x46 to { XG += memory.read16(IP + 2); IP += 18; },
            0x47 to { XH += memory.read16(IP + 2); IP += 18; },

            0x60 to { XA += memory.read4(IP + 2); IP += 6; },
            0x61 to { XB += memory.read4(IP + 2); IP += 6; },
            0x62 to { XC += memory.read4(IP + 2); IP += 6; },
            0x63 to { XD += memory.read4(IP + 2); IP += 6; },
            0x64 to { XE += memory.read4(IP + 2); IP += 6; },
            0x65 to { XF += memory.read4(IP + 2); IP += 6; },
            0x66 to { XG += memory.read4(IP + 2); IP += 6; },
            0x67 to { XH += memory.read4(IP + 2); IP += 6; },

            0x70 to { XA += memory.read1(IP + 2); IP += 3; },
            0x71 to { XB += memory.read1(IP + 2); IP += 3; },
            0x72 to { XC += memory.read1(IP + 2); IP += 3; },
            0x73 to { XD += memory.read1(IP + 2); IP += 3; },
            0x74 to { XE += memory.read1(IP + 2); IP += 3; },
            0x75 to { XF += memory.read1(IP + 2); IP += 3; },
            0x76 to { XG += memory.read1(IP + 2); IP += 3; },
            0x77 to { XH += memory.read1(IP + 2); IP += 3; },

            0xC0 to { XA -= memory.read16(IP + 2); IP += 18; },
            0xC1 to { XB -= memory.read16(IP + 2); IP += 18; },
            0xC2 to { XC -= memory.read16(IP + 2); IP += 18; },
            0xC3 to { XD -= memory.read16(IP + 2); IP += 18; },
            0xC4 to { XE -= memory.read16(IP + 2); IP += 18; },
            0xC5 to { XF -= memory.read16(IP + 2); IP += 18; },
            0xC6 to { XG -= memory.read16(IP + 2); IP += 18; },
            0xC7 to { XH -= memory.read16(IP + 2); IP += 18; },

            0xE0 to { XA -= memory.read4(IP + 2); IP += 6; },
            0xE1 to { XB -= memory.read4(IP + 2); IP += 6; },
            0xE2 to { XC -= memory.read4(IP + 2); IP += 6; },
            0xE3 to { XD -= memory.read4(IP + 2); IP += 6; },
            0xE4 to { XE -= memory.read4(IP + 2); IP += 6; },
            0xE5 to { XF -= memory.read4(IP + 2); IP += 6; },
            0xE6 to { XG -= memory.read4(IP + 2); IP += 6; },
            0xE7 to { XH -= memory.read4(IP + 2); IP += 6; },

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