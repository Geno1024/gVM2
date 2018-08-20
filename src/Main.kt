package com.geno1024.gvm

import com.geno1024.gvm.chipset.CPU
import com.geno1024.gvm.chipset.LargeMemoryCard
import com.geno1024.gvm.chipset.SmallMemoryCard
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


        memory.load(0, arrayOf(0x01, 0x60, 0x10, 0x11, 0x87, 0x53, 0x00, 0x01).map { it.toByte() }.toTypedArray())
        cpu.run()
    }
}