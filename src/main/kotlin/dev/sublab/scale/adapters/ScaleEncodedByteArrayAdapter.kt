package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.types.ScaleEncodedByteArray
import kotlin.reflect.KType

class ScaleEncodedByteArrayAdapter: ScaleCodecAdapter<ScaleEncodedByteArray>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): ScaleEncodedByteArray {
        error("shouldn't be used for reading purpose")
    }

    override fun write(obj: ScaleEncodedByteArray, type: KType, annotations: List<Annotation>) = obj.value
}