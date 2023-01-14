package dev.sublab.scale.adapters

import dev.sublab.scale.*
import dev.sublab.scale.reflection.listOfBytesType
import kotlin.reflect.KType

/**
 * An adapter for String
 */
@Suppress("unused")
class StringAdapter(
    adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<String>() {

    private val adapter = ByteArrayAdapter(adapterResolver)

    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = String(
        adapter.read(reader, listOfBytesType())
    )

    override fun write(obj: String, type: KType, annotations: List<Annotation>) = adapter.write(
        obj.toByteArray(),
        listOfBytesType()
    )
}
