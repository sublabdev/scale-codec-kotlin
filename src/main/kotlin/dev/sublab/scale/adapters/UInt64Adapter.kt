package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt64
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt64
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt64Adapter: ScaleCodecAdapter<UInt64>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(UInt64.SIZE_BYTES)
        .toUInt64()

    override fun write(obj: UInt64, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}