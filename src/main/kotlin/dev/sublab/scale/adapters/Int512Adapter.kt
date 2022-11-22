package dev.sublab.scale.adapters

import dev.sublab.common.numerics.Int512
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toInt512
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class Int512Adapter: ScaleCodecAdapter<Int512>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(Int512.SIZE_BYTES)
        .toInt512()

    override fun write(obj: Int512, type: KType) = obj.toByteArray()
}