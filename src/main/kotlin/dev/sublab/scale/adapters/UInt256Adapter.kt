package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt256
import java.math.BigInteger
import kotlin.reflect.KType

class UInt256Adapter: ScaleCodecAdapter<UInt256>() {
    override fun read(reader: ByteArrayReader, type: KType): UInt256 {
        val byteArray = reader.read(byteSize(UInt256::class))
        return UInt256(BigInteger(1, byteArray.reversedArray()))
    }

    override fun write(obj: UInt256, type: KType)
        = obj.value.toByteArray().reversedArray().copyOf(byteSize(UInt256::class))
}