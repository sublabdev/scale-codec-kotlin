package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int32
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toInt32
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class Int32Adapter: ScaleCodecAdapter<Int32>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(Int32.SIZE_BYTES)
        .toInt32()

    override fun write(obj: Int32, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}