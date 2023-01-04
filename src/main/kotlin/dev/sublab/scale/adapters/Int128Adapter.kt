package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int128
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toInt128
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class Int128Adapter: ScaleCodecAdapter<Int128>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(Int128.SIZE_BYTES)
        .toInt128()

    override fun write(obj: Int128, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}