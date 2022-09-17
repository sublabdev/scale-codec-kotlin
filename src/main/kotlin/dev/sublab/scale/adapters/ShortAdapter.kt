package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun Short.toScaleByteArray() = byteRangeForNumeric(Short::class)
    .map { toInt() shr it }
    .map { it.toByte() }
    .toByteArray()

class ShortAdapter: ScaleCodecAdapter<Short>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Short.SIZE_BYTES)
        .foldIndexed(0) { i, result, byte -> result or (byte.toUByte().toInt() shl 8 * i) }
        .toShort()

    override fun write(obj: Short, type: KType) = obj.toScaleByteArray()
}