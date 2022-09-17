package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

internal fun Byte.toScaleByteArray() = byteArrayOf(this)

class ByteAdapter: ScaleCodecAdapter<Byte>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte()
    override fun write(obj: Byte, type: KType) = obj.toScaleByteArray()
}