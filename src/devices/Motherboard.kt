package com.geno1024.gvm.devices

import com.geno1024.gvm.chipset.CPU
import com.geno1024.gvm.chipset.LargeMemoryCard

class Motherboard
{
    lateinit var cpu: CPU
    infix fun assemble(cpu: CPU) { this.cpu = cpu.apply { motherboard = this@Motherboard } }

    lateinit var memory: LargeMemoryCard
    infix fun assemble(memory: LargeMemoryCard) { this.memory = memory.apply { motherboard = this@Motherboard } }
}