package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int256
import java.math.BigInteger
import kotlin.reflect.KType

class Int256Adapter: ScaleCodecAdapter<Int256>() {
    override fun read(reader: ByteArrayReader, type: KType): Int256 {
        val byteArray = reader.read(byteSize(Int256::class))
        return Int256(BigInteger(1, byteArray.reversedArray()))
    }

    override fun write(obj: Int256, type: KType)
        = obj.value.toByteArray().reversedArray().copyOf(byteSize(Int256::class))
}