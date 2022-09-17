package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun UByte.toScaleByteArray() = byteArrayOf(toByte())

class UByteAdapter: ScaleCodecAdapter<UByte>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte().toUByte()
    override fun write(obj: UByte, type: KType) = obj.toScaleByteArray()
}