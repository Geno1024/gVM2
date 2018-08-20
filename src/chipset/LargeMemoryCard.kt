package com.geno1024.gvm.chipset

import com.geno1024.gvm.devices.Motherboard
import com.geno1024.gvm.hex16
import com.geno1024.gvm.hex2

class LargeMemoryCard constructor(val size: Long, val pageSizeBy2: Int = 30)
{
    lateinit var motherboard: Motherboard

    val pages: Int = (size shr pageSizeBy2).toInt() + if (size and ((1L shl pageSizeBy2) - 1) == 0L) 0 else 1

    val memory = Array(pages) { _ -> ByteArray(1 shl pageSizeBy2) { _ -> 0 } }

    fun read1(pos: Long): Byte = memory[(pos shr pageSizeBy2).toInt()][(pos and ((1L shl pageSizeBy2) - 1)).toInt()]

    fun write1(pos: Long, data: Byte) { memory[(pos shr pageSizeBy2).toInt()][(pos and ((1L shl pageSizeBy2) - 1)).toInt()] = data }

    fun read4(pos: Long): Int =
        (read1(pos).toInt() and 255 shl 24) +
        (read1(pos + 1).toInt() and 255 shl 16) +
        (read1(pos + 2).toInt() and 255 shl 8) +
        (read1(pos + 3).toInt() and 255)

    fun write4(pos: Long, data: Int)
    {
        write1(pos, (data shr 24).toByte())
        write1(pos + 1, (data shr 16).toByte())
        write1(pos + 2, (data shr 8).toByte())
        write1(pos + 3, (data).toByte())
    }

    fun read16(pos: Long): Long =
        (read4(pos).toLong() and 4294967295 shl 96) +
        (read4(pos + 4).toLong() and 4294967295 shl 64) +
        (read4(pos + 8).toLong() and 4294967295 shl 32) +
        (read4(pos + 12).toLong() and 4294967295)

    fun write16(pos: Long, data: Long)
    {
        write4(pos, (data shr 96).toInt())
        write4(pos + 4, (data shr 64).toInt())
        write4(pos + 8, (data shr 32).toInt())
        write4(pos + 12, data.toInt())
    }

    fun load(pos: Long, data: Array<Byte>) = data.forEachIndexed { index, datum -> write1(pos + index, datum) }

    fun load(pos: Long, data: Array<Int>) = data.forEachIndexed { index, datum -> write4(pos + 4 * index, datum) }

    fun load(pos: Long, data: Array<Long>) = data.forEachIndexed { index, datum -> write16(pos + 16 * index, datum) }

    fun dump(left: Long = 0L, right: Long = size)
    {
        // zeroth line
        println("----------------:  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f")

        // first line
        val leftLine = left and 0x7fff_ffff_ffff_fff0
        val rightLine = right and 0x7fff_ffff_ffff_fff0
        print("${leftLine.hex16()}: ")
        (leftLine until left).forEach { print("   ") }
        (left..leftLine + 15).forEach { print("${read1(it).hex2()} ") }

        // loop lines
        (leftLine + 16 until rightLine).forEach {
            if (it and 15 == 0L) print("\n${it.hex16()}: ")
            print("${read1(it).hex2()} ")
        }

        // last line
        print("\n${rightLine.hex16()}: ")
        (rightLine..right).forEach {
            print("${read1(it).hex2()} ")
        }

    }
}