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
import kotlin.reflect.full.createType

@Suppress("unused")
class ScaleCodec<Data: Any>(
    var settings: ScaleCodecSettings<Data>
) {
    companion object {
        fun default() = ScaleCodec(settings = ScaleCodecSettings.default())
        fun hex() = ScaleCodec(settings = ScaleCodecSettings.hex())
    }

    internal fun fromData(data: Data) = settings.dataContainer.toByteArray(data)
    internal fun toData(byteArray: ByteArray) = settings.dataContainer.fromByteArray(byteArray)

    /**
     * This method deserializes object of given Kotlin type
     */
    @Throws
    fun <T> fromScale(data: Data, type: KType)
        = settings.adapterProvider.findAdapter<T>(type).read(fromData(data), type)

    /**
     * This method deserializes object of given Kotlin class
     */
    @Throws
    fun <T: Any> fromScale(data: Data, type: KClass<T>)
        = fromScale<T>(data, type.createType())

    /**
     * This method serializes object of given nonnull Kotlin type
     */
    @Throws
    fun <T> toScale(obj: T, type: KType) = toData(
        settings.adapterProvider.findAdapter<T>(type).write(obj, type)
    )

    /**
     * This method serializes object of given nonnull Kotlin class
     */
    @Throws
    fun <T: Any> toScale(obj: T, type: KClass<T>)
        = toScale(obj, type.createType())

    /**
     * Grouped scale encoding
     */
    fun transaction() = ScaleCodecTransaction(this)
}