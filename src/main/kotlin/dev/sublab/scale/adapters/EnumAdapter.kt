package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class InvalidTypeException(type: KType): Throwable()
class InvalidEnumClassException(type: KClass<*>, obj: Any): Throwable()

class EnumAdapter<T: Any>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<T>() {

    @Suppress("UNCHECKED_CAST")
    @Throws(InvalidEnumClassException::class, InvalidTypeException::class)
    override fun read(reader: ByteArrayReader, type: KType): T {
        val kClass = type.classifier as? KClass<*> ?: throw InvalidTypeException(type)

        val enumInstance = readEnumClass(reader, kClass)
        if (enumInstance != null) return enumInstance

        val index = readIndex(reader)
        val nestedClass = kClass.nestedClasses.elementAt(index)

        // Read as direct "nestedClass" type, but convert to T as enum superclass type
        return adapterResolver.findAdapter(nestedClass).read(reader, nestedClass.createType()) as T
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(InvalidEnumClassException::class)
    private fun readEnumClass(reader: ByteArrayReader, type: KClass<*>): T? {
        if (!type.java.isEnum) return null

        val index = readIndex(reader)
        if (index < 0 || index >= type.java.enumConstants.size)
            throw InvalidEnumClassException(type, index)

        return type.java.enumConstants[index] as T
    }

    private fun readIndex(reader: ByteArrayReader)
        = adapterResolver.findAdapter(UByte::class).read(reader, UByte::class).toInt()

    @Throws(InvalidEnumClassException::class, InvalidTypeException::class)
    override fun write(obj: T, type: KType): ByteArray {
        val kClass = type.classifier as? KClass<*> ?: throw InvalidTypeException(type)

        val writtenData = writeEnumClass(obj, kClass)
        if (writtenData != null) return writtenData

        if (!kClass.nestedClasses.contains(obj::class)) {
            throw InvalidTypeException(type)
        }

        var byteArray = byteArrayOf()

        // Write index
        val index = kClass.nestedClasses.indexOf(obj::class).toUByte()
        byteArray += adapterResolver.findAdapter(UByte::class).write(index, UByte::class)

        // Write value
        val enumCaseType = obj::class.createType()
        byteArray += adapterResolver.findAdapter<T>(enumCaseType).write(obj, enumCaseType)

        return byteArray
    }

    @Throws(InvalidEnumClassException::class)
    private fun writeEnumClass(obj: T, type: KClass<*>): ByteArray? {
        if (!type.java.isEnum) return null

        val index = type.java.enumConstants.indexOf(obj)
        if (index == -1) throw InvalidEnumClassException(type, obj)

        return adapterResolver.findAdapter(UByte::class).write(index.toUByte(), UByte::class)
    }
}