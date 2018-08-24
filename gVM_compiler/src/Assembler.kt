package com.geno1024.gvm.compiler

object Assembler
{
    fun registerIndex(register: String): String = when (register) {
        "XA" -> "0"
        "XB" -> "1"
        "XC" -> "2"
        "XD" -> "3"
        "XE" -> "4"
        "XF" -> "5"
        "XG" -> "6"
        "XH" -> "7"
        else -> throw OperandException("Register name error.")
    }

    fun String.toGlong(): Long = if (contains('@')) // number@radix
        with(split('@', limit = 2)) {
            this[0].toLongOrNull(
                this[1].toIntOrNull()?:throw Exception("Radix")
            )?:throw Exception("Num")
        }
    else // decimal
        toLongOrNull()?:throw Exception("Num")



    fun assembleSingleLine(line: String): String
    {
        with (line.trim()
            .replaceAfter("//", "")
            .dropLastWhile { it == '/' }
            .toUpperCase()) {
                val (op, ops) = with(split(' ', limit = 2)) { this[0] to this[1].split(',').map { it.trim() } }
                val opn = ops.size
                when (op)
                {
                    "INC" -> { // 010?
                        if (opn > 1) throw OperandException("Too many operands.")
                        else if (opn == 0) throw OperandException("Lost operands.")
                        return "010${registerIndex(ops[0])}"
                    }
                    "ADD", "ADD8" -> { // 014? ????????????????
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "014${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(16, '0')}"
                    }
                    "ADD4" -> { // 015? ????????
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "015${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(8, '0')}"
                    }
                    "ADD1" -> { // 017? ??
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "017${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(2, '0')}"
                    }
                    "DEC" -> { // 018?
                        if (opn > 1) throw OperandException("Too many operands.")
                        else if (opn == 0) throw OperandException("Lost operands.")
                        return "018${registerIndex(ops[0])}"
                    }
                    "SUB", "SUB8" -> { // 01C? ????????????????
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "01C${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(16, '0')}"
                    }
                    "SUB4" -> { // 01D? ????????
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "01D${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(8, '0')}"
                    }
                    "SUB1" -> { // 01F? ??
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "01F${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(2, '0')}"
                    }
                    "MOV", "MOV8" -> { // 020? ????????????????
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "020${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(16, '0')}"
                    }
                    "MOV4" -> { // 021? ????????
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "021${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(8, '0')}"
                    }
                    "MOV1" -> { // 023? ??
                        if (opn > 2) throw OperandException("Too many operands.")
                        else if (opn < 2) throw OperandException("Too few operands.")
                        return "023${registerIndex(ops[0])}${ops[1].toGlong().toString(16).padStart(2, '0')}"
                    }
                    else -> throw OperatorException("Invalid operator.")
                }
        }
    }


    open class AssemblerException: Exception
    {
        constructor(): super()
        constructor(message: String): super(message)
        constructor(message: String, cause: Throwable): super(message, cause)
        constructor(cause: Throwable): super(cause)
    }

    class OperatorException: AssemblerException
    {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }

    class OperandException: AssemblerException
    {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }

    class NumberFormatException: AssemblerException
    {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }
}