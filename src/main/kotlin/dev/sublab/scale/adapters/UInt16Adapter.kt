package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt16
import kotlin.reflect.KType

internal fun ByteArray.toUShort() = foldIndexed(0U) { i, result, byte ->
    result or (byte.toUByte().toUInt() shl 8 * i)
}.toUShort()

internal fun UInt16.toScaleByteArray() = byteRangeForNumeric(UInt16::class)
    .map { toUInt() shr it }
    .map { it.toByte() }
    .toByteArray()

class UInt16Adapter: ScaleCodecAdapter<UInt16>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt16.SIZE_BYTES)
        .toUShort()

    override fun write(obj: UInt16, type: KType) = obj.toScaleByteArray()
}