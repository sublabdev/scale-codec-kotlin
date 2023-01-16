/**
 *
 * Copyright 2023 SUBSTRATE LABORATORY LLC <info@sublab.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class NoValueReturned(type: KClass<*>, property: KProperty<*>): Throwable()
class NoReadableVariableFound(type: KClass<*>, propertyName: String?): Throwable()

/**
 * An adapter for [Struct]
 * @property adapterResolver a scale codec adapter provider
 */
class StructAdapter<T: Any>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): T {
        val kClass = type.classifier as? KClass<T> ?: throw InvalidTypeException(type)

        val dataInstance = readDataClass(reader, kClass)
        if (dataInstance != null) return dataInstance

        val obj = (kClass.createInstance() as? T) ?: throw InvalidTypeException(type)

        val properties = kClass.memberProperties.mapNotNull { it as? KMutableProperty1<T, Any> }
        for (property in properties) {
            val propertyType = property.returnType
            val value = adapterResolver.findAdapter<Any>(propertyType)
                .read(reader, propertyType, property.annotations)
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
            injection[parameter] = adapter.read(reader, parameterType, parameter.annotations)
        }

        return constructor.callBy(injection)
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(NoValueReturned::class, NoReadableVariableFound::class)
    override fun write(obj: T, type: KType, annotations: List<Annotation>): ByteArray {
        val kClass = (obj::class as KClass<T>)

        val dataWritten = writeDataClass(obj, kClass)
        if (dataWritten != null) return dataWritten

        var byteArray = byteArrayOf()
        for (property in kClass.memberProperties) {
            val propertyType = property.returnType
            val value = property.get(obj) ?: throw NoValueReturned(kClass, property)
            byteArray += adapterResolver.findAdapter<Any>(propertyType).write(value, propertyType, annotations)
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
            byteArray += adapterResolver.findAdapter<Any>(parameterType)
                .write(value, parameterType, parameter.annotations)
        }

        return byteArray
    }
}