package com.geno1024.gvm

fun Long.hex16() = toString(16).padStart(16, '0')
fun Int.hex2() = toString(16).padStart(2, '0')
fun Byte.hex2() = toString(16).padStart(2, '0')
