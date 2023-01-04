package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int8
import dev.sublab.common.numerics.toByteArray
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class Int8Adapter: ScaleCodecAdapter<Int8>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader.readByte()
    override fun write(obj: Int8, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}