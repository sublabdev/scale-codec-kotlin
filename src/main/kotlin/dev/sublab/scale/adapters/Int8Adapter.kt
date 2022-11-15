package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int8
import kotlin.reflect.KType

internal fun Int8.toScaleByteArray() = byteArrayOf(this)

class Int8Adapter: ScaleCodecAdapter<Int8>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte()
    override fun write(obj: Int8, type: KType) = obj.toScaleByteArray()
}