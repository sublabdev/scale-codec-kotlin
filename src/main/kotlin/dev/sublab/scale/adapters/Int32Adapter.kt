package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int32
import kotlin.reflect.KType

internal fun Int32.toScaleByteArray() = byteRangeForNumeric(Int32::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class Int32Adapter: ScaleCodecAdapter<Int32>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Int32.SIZE_BYTES)
        .foldIndexed(0) { i, result, byte -> result or (byte.toUByte().toInt() shl 8 * i) }

    override fun write(obj: Int32, type: KType) = obj.toScaleByteArray()
}