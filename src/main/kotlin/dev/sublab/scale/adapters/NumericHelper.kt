package dev.sublab.scale.adapters

import dev.sublab.scale.dataTypes.*
import kotlin.reflect.KClass

class InvalidNumericType: Throwable()

internal fun byteSize(type: KClass<*>) = when (type) {
    UInt8::class, UByte::class, Int8::class, UInt8::class -> Byte.SIZE_BYTES
    Short::class, UShort::class, Int16::class, UInt16::class -> Short.SIZE_BYTES
    Int::class, UInt::class, Int32::class, UInt32::class -> Int.SIZE_BYTES
    Long::class, ULong::class, Int64::class, UInt64::class -> Long.SIZE_BYTES
    Int128::class, UInt128::class -> 128/8
    Int256::class, UInt256::class -> 256/8
    Int512::class, UInt512::class -> 512/8
    else -> throw InvalidNumericType()
}

@Throws(InvalidNumericType::class)
internal fun byteRangeForNumeric(type: KClass<*>) = (0..((byteSize(type) - 1) * 8) step 8)