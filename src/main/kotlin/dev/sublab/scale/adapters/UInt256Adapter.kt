package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt256
import dev.sublab.common.numerics.toByteArray
import dev.sublab.common.numerics.toUInt256
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt256Adapter: ScaleCodecAdapter<UInt256>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt256.SIZE_BYTES)
        .toUInt256()

    override fun write(obj: UInt256, type: KType) = obj.toByteArray()
}