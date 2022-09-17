package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun Long.toScaleByteArray() = byteRangeForNumeric(Long::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class LongAdapter: ScaleCodecAdapter<Long>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Long.SIZE_BYTES)
        .foldIndexed(0L) { i, result, byte -> result or (byte.toUByte().toLong() shl 8 * i) }

    override fun write(obj: Long, type: KType) = obj.toScaleByteArray()
}