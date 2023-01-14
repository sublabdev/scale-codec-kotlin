package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt256
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt256
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

/**
 * An adapter for UInt256
 */
class UInt256Adapter: ScaleCodecAdapter<UInt256>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(UInt256.SIZE_BYTES)
        .toUInt256()

    override fun write(obj: UInt256, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}