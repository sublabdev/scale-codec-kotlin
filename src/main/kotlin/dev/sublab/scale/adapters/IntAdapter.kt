package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun Int.toScaleByteArray() = byteRangeForNumeric(Int::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class IntAdapter: ScaleCodecAdapter<Int>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Int.SIZE_BYTES)
        .foldIndexed(0) { i, result, byte -> result or (byte.toUByte().toInt() shl 8 * i) }

    override fun write(obj: Int, type: KType) = obj.toScaleByteArray()
}