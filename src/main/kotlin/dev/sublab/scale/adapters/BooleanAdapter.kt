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
import kotlin.reflect.KType

class InvalidBooleanByteException(byte: Byte): ScaleCodecReadException("Unknown byte passed: $byte")

@Throws
private fun <T> parseBoolean(reader: ByteArrayReader, map: Map<Int, T>) = reader.readByte().let {
    if (map.containsKey(it.toInt())) map[it.toInt()]
    else throw InvalidBooleanByteException(it)
}

/**
 * An adapter for Boolean type
 */
@Suppress("unused")
class BooleanAdapter: ScaleCodecAdapter<Boolean>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = parseBoolean(reader, mapOf(
        0 to false,
        1 to true
    ))!! // even it's safe here, don't like the force unwrap, rethink

    override fun write(obj: Boolean, type: KType, annotations: List<Annotation>) = byteArrayOf(if (obj) 1 else 0)
}

/**
 * An adapter for NullableBoolean type
 */
@Suppress("unused")
class NullableBooleanAdapter: ScaleCodecAdapter<Boolean?>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = parseBoolean(reader, mapOf(
        0 to null,
        1 to true,
        2 to false
    ))

    override fun write(obj: Boolean?, type: KType, annotations: List<Annotation>) = byteArrayOf(
        obj?.let {
            if (it) 1 else 2
        } ?: 0
    )
}