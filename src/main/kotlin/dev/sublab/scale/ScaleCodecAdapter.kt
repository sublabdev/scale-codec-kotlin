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

package dev.sublab.scale

import dev.sublab.scale.adapters.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class NoAdapterKnown(val type: KType? = null): Throwable()
class NoNullableAdapterException: Throwable()

open class ScaleCodecReadException(reason: String?): Throwable(reason)
open class ScaleCodecWriteException(reason: String?): Throwable(reason)

/**
 * Provides a default interface for adapters
 */
abstract class ScaleCodecAdapter<T> {
    @Throws(ScaleCodecReadException::class)
    fun read(byteArray: ByteArray, type: KType, annotations: List<Annotation> = listOf()): T = read(
        ByteArrayReader(byteArray),
        type
    )

    // Implement in adapters

    /**
     * Reads (decodes) data to specified type
     * @return Decoded value of the provided type
     */
    @Throws(ScaleCodecReadException::class)
    abstract fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation> = listOf()): T

    /**
     * Writes (encodes) the object
     * @return The encoded ByteArray
     */
    @Throws(ScaleCodecWriteException::class)
    abstract fun write(obj: T, type: KType, annotations: List<Annotation> = listOf()): ByteArray

}

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(byteArray: ByteArray, type: KClass<T>) = read(byteArray, type.createType())

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(reader: ByteArrayReader, type: KClass<T>) = read(reader, type.createType())

@Throws(ScaleCodecWriteException::class)
fun <T: Any> ScaleCodecAdapter<T>.write(obj: T, type: KClass<T>) = write(obj, type.createType())