package com.geno1024.gvm.chipset

import com.geno1024.gvm.devices.Motherboard

class SmallMemoryCard constructor(size: Int)
{
    lateinit var motherboard: Motherboard

    val memory = ByteArray(size) { _ -> 0 }

    fun read1(pos: Int): Byte = memory[pos]

    fun write1(pos: Int, data: Byte) { memory[pos] = data }

    fun read4(pos: Int): Int = (memory[pos].toInt() shl 24) + (memory[pos + 1].toInt() shl 16) + (memory[pos + 2].toInt() shl 8) + memory[pos + 3]

    fun write4(pos: Int, data: Int)
    {
        memory[pos] = (data ushr 24).toByte()
        memory[pos + 1] = (data ushr 16).toByte()
        memory[pos + 2] = (data ushr 8).toByte()
        memory[pos + 3] = data.toByte()
    }
}