package dev.sublab.scale.adapters

import dev.sublab.scale.*
import dev.sublab.scale.reflection.listOfBytesType
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

@Suppress("unused")
class StringAdapter(
    adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<String>() {

    private val adapter = ByteArrayAdapter(adapterResolver)

    override fun read(reader: ByteArrayReader, type: KType) = String(
        adapter.read(reader, listOfBytesType())
    )

    override fun write(obj: String, type: KType) = adapter.write(obj.toByteArray(), listOfBytesType())
}
