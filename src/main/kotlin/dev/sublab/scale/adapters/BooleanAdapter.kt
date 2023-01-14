package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KType

class InvalidBooleanByteException(byte: Byte): ScaleCodecReadException("Unknown byte passed: $byte")

@Throws
private fun <T> parseBoolean(reader: ByteArrayReader, map: Map<Int, T>) = reader.readByte().let {
    if (map.containsKey(it.toInt())) map[it.toInt()]
    else throw InvalidBooleanByteException(it)
}

/**
 * An adapter for Boolean type
 */
@Suppress("unused")
class BooleanAdapter: ScaleCodecAdapter<Boolean>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = parseBoolean(reader, mapOf(
        0 to false,
        1 to true
    ))!! // even it's safe here, don't like the force unwrap, rethink

    override fun write(obj: Boolean, type: KType, annotations: List<Annotation>) = byteArrayOf(if (obj) 1 else 0)
}

/**
 * An adapter for NullableBoolean type
 */
@Suppress("unused")
class NullableBooleanAdapter: ScaleCodecAdapter<Boolean?>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = parseBoolean(reader, mapOf(
        0 to null,
        1 to true,
        2 to false
    ))

    override fun write(obj: Boolean?, type: KType, annotations: List<Annotation>) = byteArrayOf(
        obj?.let {
            if (it) 1 else 2
        } ?: 0
    )
}