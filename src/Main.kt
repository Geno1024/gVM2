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


        memory.load(0, arrayOf<Byte>(0x00, 0x01))
        memory.dump(1, 45)
//        cpu.run()
    }
}