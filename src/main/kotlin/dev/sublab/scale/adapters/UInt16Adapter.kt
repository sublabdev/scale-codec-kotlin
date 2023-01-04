package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt16
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt16
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt16Adapter: ScaleCodecAdapter<UInt16>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(UInt16.SIZE_BYTES)
        .toUInt16()

    override fun write(obj: UInt16, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}