package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int16
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toInt16
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class Int16Adapter: ScaleCodecAdapter<Int16>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Int16.SIZE_BYTES)
        .toInt16()

    override fun write(obj: Int16, type: KType) = obj.toByteArray()
}