package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun ByteArray.toUInt() = foldIndexed(0U) { i, result, byte ->
    result or (byte.toUByte().toUInt() shl 8 * i)
}

internal fun UInt.toScaleByteArray() = byteRangeForNumeric(UInt::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class UIntAdapter: ScaleCodecAdapter<UInt>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt.SIZE_BYTES)
        .toUInt()

    override fun write(obj: UInt, type: KType) = obj.toScaleByteArray()
}