package dev.sublab.scale.adapters

import dev.sublab.scale.*
import dev.sublab.scale.reflection.*
import java.math.BigInteger
import kotlin.reflect.KType

@Suppress("unused")
class ListAdapter<T>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<List<T>>() {

    @Throws(UnwrappingException::class)
    override fun read(reader: ByteArrayReader, type: KType): List<T> {
        val wrappedType = type.unwrapArgument()
        val count = adapterResolver.findAdapter(BigInteger::class).read(reader, BigInteger::class).toInt()

        return (0 until count)
            .map { adapterResolver.findAdapter<T>(wrappedType).read(reader, wrappedType) }
            .toList()
    }

    override fun write(obj: List<T>, type: KType) = obj.map {
        adapterResolver.findAdapter<T>(type.unwrapArgument()).write(it, type.unwrapArgument())
    }.fold(adapterResolver.findAdapter(BigInteger::class).write(obj.count().toBigInteger(), BigInteger::class)) { byteArray, it ->
        byteArray + it
    }
}