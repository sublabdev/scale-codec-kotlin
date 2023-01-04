package dev.sublab.scale.adapters

import dev.sublab.common.numerics.*
import dev.sublab.scale.*
import java.math.BigInteger
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.reflect.KType

val MAX_BIG_INTEGER_SCALE_VALUE: BigInteger = BigInteger.TWO
    .pow(536)
    .subtract(BigInteger.ONE)

class BigIntegerValueTooBig: Throwable()
class NegativeBigIntegerNotSupported: Throwable()

private val modesByMinValue: Map<BigInteger, Byte> = mapOf(
    Pair(BigInteger((1U shl (UInt.SIZE_BITS - 2)).toString()), 0b11),
    Pair(BigInteger((1U shl (UShort.SIZE_BITS - 2)).toString()), 0b10),
    Pair(BigInteger((1U shl (UByte.SIZE_BITS - 2)).toString()), 0b01)
)

private fun detectMode(obj: BigInteger): Byte {
    for ((minValue, mode) in modesByMinValue) {
        if (obj >= minValue) {
            return mode
        }
    }

    return 0b00
}

private fun ByteArray.fillingWithZeroesUntilWidth(width: Int): ByteArray {
    val countOfZeroesToAdd = width - size
    return if (countOfZeroesToAdd > 0) {
        this + (0 until countOfZeroesToAdd).map { 0 }
    } else {
        this
    }
}

fun ByteArray.clampedToBigInteger() = BigInteger(when {
    size <= UByte.SIZE_BYTES -> first().toUByte()
    size <= UInt16.SIZE_BYTES -> fillingWithZeroesUntilWidth(UInt16.SIZE_BYTES).toUInt16()
    size <= UInt32.SIZE_BYTES -> fillingWithZeroesUntilWidth(UInt32.SIZE_BYTES).toUInt32()
    size <= UInt64.SIZE_BYTES -> fillingWithZeroesUntilWidth(UInt64.SIZE_BYTES).toUInt64()
    else -> BigInteger(this)
}.toString())

fun BigInteger.toClampedByteArray(): ByteArray {
    val byteArray = when {
        this < BigInteger((1 shl UByte.SIZE_BITS).toString()) -> toByte().toUByte().toByteArray()
        this < BigInteger((1 shl UInt16.SIZE_BITS).toString()) -> toShort().toUShort().toByteArray()
        this < BigInteger((1 shl UInt32.SIZE_BITS).toString()) -> toInt().toUInt().toByteArray()
        this < BigInteger((1 shl UInt64.SIZE_BITS).toString()) -> toLong().toULong().toByteArray()
        else -> toByteArray().reversedArray()
    }

    val zeroOffset = byteArray.indexOfLast { it > 0.toByte() } + 1
    return byteArray.copyOfRange(0, zeroOffset)
}

class BigIntegerAdapter(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<BigInteger>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): BigInteger {
        val byte = reader.readByte()
        val mode = byte and 0b11
        val value = byte or 0b11

        fun readByteArray(typeBytes: Int) = byteArrayOf(value) + reader.read(typeBytes - 1)

        return BigInteger(when (mode.toInt()) {
            0b00 -> (value.toUByte().toUInt() shr 2)
            0b01 -> readByteArray(UShort.SIZE_BYTES).toUShort().toUInt() shr 2
            0b10 -> readByteArray(UInt.SIZE_BYTES).toUInt() shr 2
            else -> reader.read((value.toInt() shr 2) + 4).clampedToBigInteger()
        }.toString())
    }

    @Throws(NegativeBigIntegerNotSupported::class)
    override fun write(obj: BigInteger, type: KType, annotations: List<Annotation>): ByteArray {
        if (obj.signum() < 0) {
            throw NegativeBigIntegerNotSupported()
        }

        if (obj > MAX_BIG_INTEGER_SCALE_VALUE) {
            throw BigIntegerValueTooBig()
        }

        val mode = detectMode(obj)
        return when (mode.toInt()) {
            0b00 -> adapterResolver.findAdapter(UByte::class)
                .write((obj.toInt().toUInt() shl 2).toUByte(), UByte::class)
            0b01 -> adapterResolver.findAdapter(UShort::class)
                .write((obj.toInt().toUInt() shl 2 or mode.toUInt()).toUShort(), UShort::class)
            0b10 -> adapterResolver.findAdapter(UInt::class)
                .write(obj.toInt().toUInt() shl 2 or mode.toUInt(), UInt::class)
            else -> {
                val result = obj.toClampedByteArray()
                val count = (result.size - 4) shl 2 or 0b11

                val prefix = adapterResolver.findAdapter(UByte::class)
                    .write(count.toUByte(), UByte::class)

                return prefix + result
            }
        }
    }
}