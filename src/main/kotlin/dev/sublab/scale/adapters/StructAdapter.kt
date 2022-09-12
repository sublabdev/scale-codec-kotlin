package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class NoValueReturned(type: KClass<*>, property: KProperty<*>): Throwable()
class NoReadableVariableFound(type: KClass<*>, propertyName: String?): Throwable()

class StructAdapter<T: Any>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun read(reader: ByteArrayReader, type: KType): T {
        val kClass = type.classifier as? KClass<T> ?: throw InvalidTypeException()

        val dataInstance = readDataClass(reader, kClass)
        if (dataInstance != null) return dataInstance

        val obj = (kClass.createInstance() as? T) ?: throw InvalidTypeException()

        val properties = kClass.memberProperties.mapNotNull { it as? KMutableProperty1<T, Any> }
        for (property in properties) {
            val propertyType = property.returnType
            val value = adapterResolver.findAdapter<Any>(propertyType).read(reader, propertyType)
            property.set(obj, value)
        }

        return obj
    }

    private fun readDataClass(reader: ByteArrayReader, type: KClass<T>): T? {
        if (!type.isData) return null
        val constructor = type.primaryConstructor ?: return null

        val injection = mutableMapOf<KParameter, Any?>()
        for (parameter in constructor.parameters) {
            val parameterType = parameter.type
            val adapter = adapterResolver.findAdapter<Any>(parameterType)
            injection[parameter] = adapter.read(reader, parameterType)
        }

        return constructor.callBy(injection)
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(NoValueReturned::class, NoReadableVariableFound::class)
    override fun write(obj: T, type: KType): ByteArray {
        val kClass = (obj::class as KClass<T>)
        val dataWritten = writeDataClass(obj, kClass)
        if (dataWritten != null) return dataWritten

        var byteArray = byteArrayOf()
        for (property in kClass.memberProperties) {
            val propertyType = property.returnType
            val value = property.get(obj) ?: throw NoValueReturned(kClass, property)
            byteArray += adapterResolver.findAdapter<Any>(propertyType).write(value, propertyType)
//            println("Property ${property.name} value ${property.get(obj)} written: ${adapterResolver.findAdapter<Any>(propertyType).write(value, propertyType).toHex()}")
        }

        return byteArray
    }

    @Throws(NoValueReturned::class, NoReadableVariableFound::class)
    private fun writeDataClass(obj: T, type: KClass<T>): ByteArray? {
        if (!type.isData) return null
        val constructor = type.primaryConstructor ?: return null

        var byteArray = byteArrayOf()
        for (parameter in constructor.parameters) {
            val parameterType = parameter.type
            val property = type.memberProperties.firstOrNull { it.name == parameter.name }
                ?: throw NoReadableVariableFound(type, parameter.name)

            val value = property.get(obj) ?: throw NoValueReturned(type, property)
            byteArray += adapterResolver.findAdapter<Any>(parameterType).write(value, parameterType)
        }

        return byteArray
    }
}