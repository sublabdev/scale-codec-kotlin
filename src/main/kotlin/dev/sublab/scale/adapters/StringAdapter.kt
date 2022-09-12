package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

private fun listOfBytesType() = List::class.createType(
    arguments = listOf(
        KTypeProjection.invariant(Byte::class.createType())
    )
)

@Suppress("unused")
class StringAdapter(
    adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<String>() {

    private val adapter = ListAdapter<Byte>(adapterResolver)

    override fun read(reader: ByteArrayReader, type: KType) = String(
        adapter.read(reader, listOfBytesType()).toByteArray()
    )

    override fun write(obj: String, type: KType) = adapter.write(obj.toByteArray().toList(), listOfBytesType())
}
