package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int512
import java.math.BigInteger
import kotlin.reflect.KType

class Int512Adapter: ScaleCodecAdapter<Int512>() {
    override fun read(reader: ByteArrayReader, type: KType): Int512 {
        val byteArray = reader.read(byteSize(Int512::class))
        return Int512(BigInteger(1, byteArray.reversedArray()))
    }

    override fun write(obj: Int512, type: KType)
        = obj.value.toByteArray().reversedArray().copyOf(byteSize(Int512::class))
}