package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class InvalidTypeException: Throwable()

class EnumAdapter<T: Any>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<T>() {

    @Suppress("UNCHECKED_CAST")
    @Throws(InvalidTypeException::class)
    override fun read(reader: ByteArrayReader, type: KType): T {
        val kClass = type.classifier as? KClass<*> ?: throw InvalidTypeException()

        val index = adapterResolver.findAdapter(Int::class).read(reader, Int::class)
        val nestedClass = kClass.nestedClasses.elementAt(index)

        // Read as direct "nestedClass" type, but convert to T as enum superclass type
        return adapterResolver.findAdapter(nestedClass).read(reader, nestedClass.createType()) as T
    }

    @Throws(InvalidTypeException::class)
    override fun write(obj: T, type: KType): ByteArray {
        val kClass = type.classifier as? KClass<*> ?: throw InvalidTypeException()
        if (!kClass.nestedClasses.contains(obj::class)) {
            throw InvalidTypeException()
        }

        var byteArray = byteArrayOf()

        // Write index
        val index = kClass.nestedClasses.indexOf(obj::class)
        byteArray += adapterResolver.findAdapter(Int::class).write(index, Int::class)

        // Write value
        val enumCaseType = obj::class.createType()
        byteArray += adapterResolver.findAdapter<T>(enumCaseType).write(obj, enumCaseType)

        return byteArray
    }
}