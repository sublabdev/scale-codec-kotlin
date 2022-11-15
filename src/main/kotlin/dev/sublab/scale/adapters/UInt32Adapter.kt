package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt32
import kotlin.reflect.KType

internal fun ByteArray.toUInt() = foldIndexed(0U) { i, result, byte ->
    result or (byte.toUByte().toUInt() shl 8 * i)
}

internal fun UInt32.toScaleByteArray() = byteRangeForNumeric(UInt32::class)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class UInt32Adapter: ScaleCodecAdapter<UInt32>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt32.SIZE_BYTES)
        .toUInt()

    override fun write(obj: UInt32, type: KType) = obj.toScaleByteArray()
}