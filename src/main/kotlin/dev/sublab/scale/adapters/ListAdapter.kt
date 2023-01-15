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
import dev.sublab.scale.annotations.FixedArray
import dev.sublab.scale.reflection.*
import java.math.BigInteger
import kotlin.reflect.KType

/**
 * An adapter for [List]
 * @property adapterResolver a scale codec adapter provider
 */
@Suppress("unused")
class ListAdapter<T>(
    private val adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<List<T>>() {

    @Throws(UnwrappingException::class)
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): List<T> {
        val wrappedType = type.unwrapArgument()

        val count = type.annotations.union(annotations).map { it as? FixedArray }.firstOrNull()?.size
            ?: adapterResolver.findAdapter(BigInteger::class).read(reader, BigInteger::class).toInt()

        return (0 until count)
            .map { adapterResolver.findAdapter<T>(wrappedType).read(reader, wrappedType) }
            .toList()
    }

    private fun initialByteArray(obj: List<T>, type: KType, annotations: List<Annotation>)
        = type.annotations.union(annotations).firstOrNull { it is FixedArray }?.let {
            byteArrayOf()
        } ?: adapterResolver.findAdapter(BigInteger::class).write(obj.count().toBigInteger(), BigInteger::class)

    override fun write(obj: List<T>, type: KType, annotations: List<Annotation>) = obj.map {
        adapterResolver.findAdapter<T>(type.unwrapArgument()).write(it, type.unwrapArgument())
    }.fold(initialByteArray(obj, type, annotations)) { byteArray, it ->
        byteArray + it
    }
}