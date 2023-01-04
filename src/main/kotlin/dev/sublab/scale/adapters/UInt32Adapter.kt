package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt32
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt32
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt32Adapter: ScaleCodecAdapter<UInt32>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(UInt32.SIZE_BYTES)
        .toUInt32()

    override fun write(obj: UInt32, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}