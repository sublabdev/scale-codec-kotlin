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
import dev.sublab.scale.types.ScaleEncodedByteArray
import kotlin.reflect.KType

/**
 * An adapter for scale encoded ByteArray
 */
class ScaleEncodedByteArrayAdapter: ScaleCodecAdapter<ScaleEncodedByteArray>() {
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): ScaleEncodedByteArray {
        error("shouldn't be used for reading purpose")
    }

    override fun write(obj: ScaleEncodedByteArray, type: KType, annotations: List<Annotation>) = obj.value
}