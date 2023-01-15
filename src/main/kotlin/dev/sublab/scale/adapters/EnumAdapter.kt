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
import dev.sublab.scale.annotations.EnumCase
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation

@Suppress("unused")
class InvalidTypeException(type: KType): Throwable()

@Suppress("unused")
class InvalidEnumClassException(kClass: KClass<*>? = null, type: KType? = null, obj: Any): Throwable()

/**
 * An adapter for [Enum]
 * @property adapterResolver a scale codec adapter provider
 */
class EnumAdapter<T: Any>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<T>() {

    @Suppress("UNCHECKED_CAST")
    @Throws(InvalidEnumClassException::class, InvalidTypeException::class)
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): T {
        val kClass = type.classifier as? KClass<*> ?: throw InvalidTypeException(type)

        val enumInstance = readEnumClass(reader, kClass)
        if (enumInstance != null) return enumInstance

        val index = readIndex(reader)
        val nestedClass = kClass.nestedClasses.firstOrNull {
            it.findAnnotation<EnumCase>()?.index == index
        } ?: run {
            throw InvalidEnumClassException(type = type, obj = index)
        }

        // Read as direct "nestedClass" type, but convert to T as enum superclass type
        return adapterResolver.findAdapter(nestedClass).read(reader, nestedClass.createType()) as T
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(InvalidEnumClassException::class)
    private fun readEnumClass(reader: ByteArrayReader, type: KClass<*>): T? {
        if (!type.java.isEnum) return null

        val index = readIndex(reader)
        if (index < 0 || index >= type.java.enumConstants.size)
            throw InvalidEnumClassException(kClass = type, obj = index)

        return type.java.enumConstants[index] as T
    }

    private fun readIndex(reader: ByteArrayReader)
        = adapterResolver.findAdapter(UByte::class).read(reader, UByte::class).toInt()

    @Throws(InvalidEnumClassException::class, InvalidTypeException::class)
    override fun write(obj: T, type: KType, annotations: List<Annotation>): ByteArray {
        val kClass = type.classifier as? KClass<*> ?: throw InvalidTypeException(type)

        val writtenData = writeEnumClass(obj, kClass)
        if (writtenData != null) return writtenData

        var byteArray = byteArrayOf()

        // Write index
        val index = kClass.nestedClasses
            .firstOrNull { it == obj::class }
            ?.let { it.findAnnotation<EnumCase>()?.index }
            ?: throw InvalidEnumClassException(type = type, obj = obj)
        byteArray += adapterResolver.findAdapter(UByte::class).write(index.toUByte(), UByte::class)

        // Write value
        val enumCaseType = obj::class.createType()
        byteArray += adapterResolver.findAdapter<T>(enumCaseType).write(obj, enumCaseType)

        return byteArray
    }

    @Throws(InvalidEnumClassException::class)
    private fun writeEnumClass(obj: T, type: KClass<*>): ByteArray? {
        if (!type.java.isEnum) return null

        val index = type.java.enumConstants.indexOf(obj)
        if (index == -1) throw InvalidEnumClassException(kClass = type, obj = obj)

        return adapterResolver.findAdapter(UByte::class).write(index.toUByte(), UByte::class)
    }
}