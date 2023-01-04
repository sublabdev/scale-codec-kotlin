package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterProvider
import dev.sublab.scale.reflection.listOfBytesType
import kotlin.reflect.KType

class ByteArrayAdapter(
    adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<ByteArray>() {
    private val adapter = ListAdapter<Byte>(adapterResolver)

    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = adapter.read(
        reader,
        listOfBytesType()
    ).toByteArray()
    
    override fun write(obj: ByteArray, type: KType, annotations: List<Annotation>) = adapter.write(
        obj.toList(),
        listOfBytesType()
    )
}