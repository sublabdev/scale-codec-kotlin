package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun ByteArray.toULong() = foldIndexed(0UL) { i, result, byte ->
    result or (byte.toUByte().toULong() shl 8 * i)
}

internal fun ULong.toScaleByteArray() = byteRangeForNumeric(ULong::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class ULongAdapter: ScaleCodecAdapter<ULong>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(ULong.SIZE_BYTES)
        .toULong()

    override fun write(obj: ULong, type: KType) = obj.toScaleByteArray()
}