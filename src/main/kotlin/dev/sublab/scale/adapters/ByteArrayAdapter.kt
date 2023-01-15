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

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterProvider
import dev.sublab.scale.reflection.listOfBytesType
import kotlin.reflect.KType

/**
 * An adapter to handle read and write operations for [ByteArray]
 */
class ByteArrayAdapter(
    adapterResolver: ScaleCodecAdapterProvider
): ScaleCodecAdapter<ByteArray>() {
    private val adapter = ListAdapter<Byte>(adapterResolver)

    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>) = adapter.read(
        reader,
        listOfBytesType()
    ).toByteArray()
    
    override fun write(obj: ByteArray, type: KType, annotations: List<Annotation>) = adapter.write(
        obj.toList(),
        listOfBytesType()
    )
}