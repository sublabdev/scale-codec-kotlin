package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt512
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt512
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt512Adapter: ScaleCodecAdapter<UInt512>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt512.SIZE_BYTES)
        .toUInt512()

    override fun write(obj: UInt512, type: KType) = obj.toByteArray()
}