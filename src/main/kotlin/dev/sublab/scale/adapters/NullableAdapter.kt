package dev.sublab.scale.adapters

import dev.sublab.scale.*
import dev.sublab.scale.reflection.*
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class InvalidNullableByteArrayException: Throwable()

@Suppress("unused")
class NullableAdapter<T>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<T?>() {

    @Throws(InvalidTypeException::class, UnwrappingException::class)
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): T? = when (reader.readByte().toInt()) {
        0 -> null
        1 -> {
            if (!type.isMarkedNullable) throw InvalidTypeException(type)
            val wrappedType = type.classifier?.createType() ?: throw InvalidTypeException(type)
            adapterResolver.findAdapter<T>(wrappedType).read(reader, wrappedType)
        }
        else -> throw InvalidNullableByteArrayException()
    }

    @Throws(InvalidTypeException::class)
    override fun write(obj: T?, type: KType, annotations: List<Annotation>): ByteArray {
        if (!type.isMarkedNullable) throw InvalidTypeException(type)
        val wrappedType = type.classifier?.createType() ?: throw InvalidTypeException(type)

        var byteArray = byteArrayOf(obj?.let { 1 } ?: 0)
        obj?.let {
            byteArray += adapterResolver.findAdapter<T>(wrappedType).write(it, wrappedType)
        }

        return byteArray
    }
}