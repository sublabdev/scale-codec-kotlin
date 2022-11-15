package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int64
import kotlin.reflect.KType

internal fun Int64.toScaleByteArray() = byteRangeForNumeric(Int64::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class Int64Adapter: ScaleCodecAdapter<Int64>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Int64.SIZE_BYTES)
        .foldIndexed(0L) { i, result, byte -> result or (byte.toUByte().toLong() shl 8 * i) }

    override fun write(obj: Int64, type: KType) = obj.toScaleByteArray()
}