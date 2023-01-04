package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt128
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt128
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt128Adapter: ScaleCodecAdapter<UInt128>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = reader
        .read(UInt128.SIZE_BYTES)
        .toUInt128()

    override fun write(obj: UInt128, type: KType, annotations: List<Annotation>) = obj.toByteArray()
}