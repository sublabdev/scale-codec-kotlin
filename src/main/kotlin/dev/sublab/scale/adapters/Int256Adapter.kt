package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int256
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toInt256
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

/**
 * An adapter for Int256
 */
class Int256Adapter: ScaleCodecAdapter<Int256>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(Int256.SIZE_BYTES)
        .toInt256()

    override fun write(obj: Int256, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}