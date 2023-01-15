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
import dev.sublab.scale.reflection.*
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class InvalidNullableByteArrayException: Throwable()

/**
 * An adapter a generic nullable type
 * @property adapterResolver a scale codec adapter provider
 */
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