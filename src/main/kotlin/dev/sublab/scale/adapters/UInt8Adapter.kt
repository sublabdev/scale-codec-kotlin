package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt8
import kotlin.reflect.KType

internal fun UInt8.toScaleByteArray() = byteArrayOf(toByte())

class UInt8Adapter: ScaleCodecAdapter<UInt8>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte().toUByte()
    override fun write(obj: UInt8, type: KType) = obj.toScaleByteArray()
}