package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.Int128
import java.math.BigInteger
import kotlin.reflect.KType

class Int128Adapter: ScaleCodecAdapter<Int128>() {
    override fun read(reader: ByteArrayReader, type: KType): Int128 {
        val byteArray = reader.read(byteSize(Int128::class))
        return Int128(BigInteger(1, byteArray.reversedArray()))
    }

    override fun write(obj: Int128, type: KType)
        = obj.value.toByteArray().reversedArray().copyOf(byteSize(Int128::class))
}