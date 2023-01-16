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
import dev.sublab.scale.NoAdapterKnown
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.default.AdapterProvider
import kotlin.reflect.KType

/**
 * A generic adapter provider
 * @property adapterProviders a list of adapter providers
 * @property onMatch a block called when an adapter provider is found
 */
internal class GenericAdapterProvider<T>(
    val adapterProviders: List<AdapterProvider>,
    val onMatch: (AdapterProvider) -> Unit
): ScaleCodecAdapter<T>() {

    @Throws(NoAdapterKnown::class)
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): T {
        for (provider in adapterProviders) {
            val adapter = provider.get<T>() ?: continue

            val offsetBeforeTry = reader.offset

            runCatching {
                adapter.read(reader, type)
            }.onSuccess {
                onMatch(provider)
                return it
            }.onFailure {
                // Reset offset to position before failed reading
                reader.offset = offsetBeforeTry
            }
        }

        throw NoAdapterKnown(type = type)
    }

    override fun write(obj: T, type: KType, annotations: List<Annotation>): ByteArray {
        for (provider in adapterProviders) {
            val adapter = provider.get<T>() ?: continue
            val byteArray = runCatching { adapter.write(obj, type) }.getOrNull()
            if (byteArray != null) {
                onMatch(provider)
                return byteArray
            }
        }

        throw NoAdapterKnown(type = type)
    }
}