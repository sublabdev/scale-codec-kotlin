package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun ByteArray.toUShort() = foldIndexed(0U) { i, result, byte ->
    result or (byte.toUByte().toUInt() shl 8 * i)
}.toUShort()

internal fun UShort.toScaleByteArray() = byteRangeForNumeric(UShort::class)
    .map { toUInt() shr it }
    .map { it.toByte() }
    .toByteArray()

class UShortAdapter: ScaleCodecAdapter<UShort>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UShort.SIZE_BYTES)
        .toUShort()

    override fun write(obj: UShort, type: KType) = obj.toScaleByteArray()
}