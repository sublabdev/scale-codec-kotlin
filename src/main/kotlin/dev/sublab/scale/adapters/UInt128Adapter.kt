package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.dataTypes.UInt128
import java.math.BigInteger
import kotlin.reflect.KType

class UInt128Adapter: ScaleCodecAdapter<UInt128>() {
    override fun read(reader: ByteArrayReader, type: KType): UInt128 {
        val byteArray = reader.read(byteSize(UInt128::class))
        return UInt128(BigInteger(1, byteArray.reversedArray()))
    }

    override fun write(obj: UInt128, type: KType)
        = obj.value.toByteArray().reversedArray().copyOf(byteSize(UInt128::class))
}