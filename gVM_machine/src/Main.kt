package com.geno1024.gvm

import com.geno1024.gvm.chipset.CPU
import com.geno1024.gvm.chipset.LargeMemoryCard
import com.geno1024.gvm.chipset.SmallMemoryCard
import com.geno1024.gvm.compiler.Assembler
import com.geno1024.gvm.devices.Motherboard

object Main
{
    @JvmStatic
    fun main(args: Array<String>)
    {
        val motherboard = Motherboard()
        val cpu = CPU(DEBUG = true)
        val memory = LargeMemoryCard(1048576, 10)
        motherboard assemble memory
        motherboard assemble cpu


        memory.load(0, Assembler.string2bin(Assembler.assembleLines(
            "ASN XA, 1@16\n" +
                "MUL XA, 10@16\n" +
                "ADD XA, A@16\n" +
                "STOP XA")))
//        memory.load(0, arrayOf(0x02, 0x00, 0x10, 0x11, 0x87, 0x53, 0xff, 0x10, 0x23, 0x45, 0x00, 0x01).map { it.toByte() }.toTypedArray())
        cpu.run()
    }
}