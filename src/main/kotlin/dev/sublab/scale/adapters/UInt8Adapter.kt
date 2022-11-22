package dev.sublab.scale.adapters

import dev.sublab.common.numerics.UInt8
import dev.sublab.common.numerics.toByteArray
import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import kotlin.reflect.KType

class UInt8Adapter: ScaleCodecAdapter<UInt8>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte().toUByte()
    override fun write(obj: UInt8, type: KType) = obj.toByteArray()
}