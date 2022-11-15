package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt64
import kotlin.reflect.KType

internal fun ByteArray.toULong() = foldIndexed(0UL) { i, result, byte ->
    result or (byte.toUByte().toULong() shl 8 * i)
}

internal fun UInt64.toScaleByteArray() = byteRangeForNumeric(UInt64::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class UInt64Adapter: ScaleCodecAdapter<UInt64>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt64.SIZE_BYTES)
        .toULong()

    override fun write(obj: UInt64, type: KType) = obj.toScaleByteArray()
}