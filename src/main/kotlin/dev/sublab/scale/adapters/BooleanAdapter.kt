package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KType

class InvalidBooleanByteException(byte: Byte): ScaleCodecReadException("Unknown byte passed: $byte")

@Throws
private fun <T> parseBoolean(reader: ByteArrayReader, map: Map<Int, T>) = reader.readByte().let {
    if (map.containsKey(it.toInt())) map[it.toInt()]
    else throw InvalidBooleanByteException(it)
}

@Suppress("unused")
class BooleanAdapter: ScaleCodecAdapter<Boolean>() {
    override fun read(reader: ByteArrayReader, type: KType) = parseBoolean(reader, mapOf(
        0 to false,
        1 to true
    ))!! // even it's safe here, don't like the force unwrap, rethink

    override fun write(obj: Boolean, type: KType) = byteArrayOf(if (obj) 1 else 0)
}

@Suppress("unused")
class OptionalBooleanAdapter: ScaleCodecAdapter<Boolean?>() {
    override fun read(reader: ByteArrayReader, type: KType) = parseBoolean(reader, mapOf(
        0 to null,
        1 to true,
        2 to false
    ))

    override fun write(obj: Boolean?, type: KType) = byteArrayOf(
        obj?.let {
            if (it) 1 else 2
        } ?: 0
    )
}