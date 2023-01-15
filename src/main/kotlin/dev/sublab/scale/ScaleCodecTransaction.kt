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

import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Handles Scale Codec transaction for generic types. Provides a mechanism for encoding and appending multiple
 * values and decode them.
 */
class ScaleCodecTransaction<Data: Any>(private val codec: ScaleCodec<Data>) {
    private var byteArray = byteArrayOf()

    /**
     * Appends additional generic objects to existing encoded data
     * @param obj a generic obj to be appended
     * @return Returns `self` with updated encoded data
     */
    @Throws
    fun <T> append(obj: T, type: KType) = apply {
        byteArray += codec.fromData(codec.toScale(obj, type))
    }

    /**
     * Appends additional generic objects to existing encoded data
     * @param obj a generic obj to be appended
     * @return Returns `self` with updated encoded data
     */
    @Throws
    fun <T: Any> append(obj: T, type: KClass<T>) = apply {
        byteArray += codec.fromData(codec.toScale(obj, type))
    }

    /**
     * Decodes the existing encoded data
     * @return An accumulated data of previously encoded objects
     */
    fun commit() = codec.toData(byteArray)
}