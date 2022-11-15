package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt512
import java.math.BigInteger
import kotlin.reflect.KType

class UInt512Adapter: ScaleCodecAdapter<UInt512>() {
    override fun read(reader: ByteArrayReader, type: KType): UInt512 {
        val byteArray = reader.read(byteSize(UInt512::class))
        return UInt512(BigInteger(1, byteArray.reversedArray()))
    }

    override fun write(obj: UInt512, type: KType)
        = obj.value.toByteArray().reversedArray().copyOf(byteSize(UInt512::class))
}