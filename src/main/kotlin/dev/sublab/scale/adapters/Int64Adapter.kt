package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int64
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toInt64
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

/**
 * An adapter for Int64
 */
class Int64Adapter: ScaleCodecAdapter<Int64>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(Int64.SIZE_BYTES)
        .toInt64()

    override fun write(obj: Int64, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}