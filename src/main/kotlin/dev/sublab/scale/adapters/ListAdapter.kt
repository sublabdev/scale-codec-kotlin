package dev.sublab.scale.adapters

import dev.sublab.scale.*
import dev.sublab.scale.annotations.FixedArray
import dev.sublab.scale.reflection.*
import java.math.BigInteger
import kotlin.reflect.KType

@Suppress("unused")
class ListAdapter<T>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<List<T>>() {

    @Throws(UnwrappingException::class)
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): List<T> {
        val wrappedType = type.unwrapArgument()

        val count = type.annotations.union(annotations).map { it as? FixedArray }.firstOrNull()?.size
            ?: adapterResolver.findAdapter(BigInteger::class).read(reader, BigInteger::class).toInt()

        return (0 until count)
            .map { adapterResolver.findAdapter<T>(wrappedType).read(reader, wrappedType) }
            .toList()
    }

    private fun initialByteArray(obj: List<T>, type: KType, annotations: List<Annotation>)
        = type.annotations.union(annotations).firstOrNull { it is FixedArray }?.let {
            byteArrayOf()
        } ?: adapterResolver.findAdapter(BigInteger::class).write(obj.count().toBigInteger(), BigInteger::class)

    override fun write(obj: List<T>, type: KType, annotations: List<Annotation>) = obj.map {
        adapterResolver.findAdapter<T>(type.unwrapArgument()).write(it, type.unwrapArgument())
    }.fold(initialByteArray(obj, type, annotations)) { byteArray, it ->
        byteArray + it
    }
}