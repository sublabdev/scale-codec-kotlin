package dev.sublab.scale.adapters

import kotlin.reflect.KClass

class InvalidNumericType: Throwable()

private fun byteSize(type: KClass<*>) = when (type) {
    Byte::class, UByte::class -> Byte.SIZE_BYTES
    Short::class, UShort::class -> Short.SIZE_BYTES
    Int::class, UInt::class -> Int.SIZE_BYTES
    Long::class, ULong::class -> Long.SIZE_BYTES
    else -> throw InvalidNumericType()
}

@Throws(InvalidNumericType::class)
internal fun byteRangeForNumeric(type: KClass<*>) = (0..((byteSize(type) - 1) * 8) step 8)