package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int16
import kotlin.reflect.KType

internal fun Int16.toScaleByteArray() = byteRangeForNumeric(Int16::class)
    .map { toInt() shr it }
    .map { it.toByte() }
    .toByteArray()

class Int16Adapter: ScaleCodecAdapter<Int16>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Int16.SIZE_BYTES)
        .foldIndexed(0) { i, result, byte -> result or (byte.toUByte().toInt() shl 8 * i) }
        .toShort()

    override fun write(obj: Int16, type: KType) = obj.toScaleByteArray()
}