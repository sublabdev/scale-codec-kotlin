package dev.sublab.scale.adapters

import dev.sublab.scale.*
import dev.sublab.scale.reflection.*
import kotlin.reflect.KType

@Suppress("unused")
class ListAdapter<T>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<List<T>>() {

    @Throws(UnwrappingException::class)
    override fun read(reader: ByteArrayReader, type: KType): List<T> {
        val wrappedType = type.unwrapArgument()

        // TODO: Migrate count to BigInteger
        val count = adapterResolver.findAdapter(Int::class).read(reader, Int::class)
        return (0 until count)
            .map { adapterResolver.findAdapter<T>(wrappedType).read(reader, wrappedType) }
            .toList()
    }

    override fun write(obj: List<T>, type: KType) = obj.map {
        adapterResolver.findAdapter<T>(type.unwrapArgument()).write(it, type.unwrapArgument())
        // TODO: Migrate count to BigInteger
    }.fold(adapterResolver.findAdapter(Int::class).write(obj.count(), Int::class)) { byteArray, it ->
        byteArray + it
    }
}