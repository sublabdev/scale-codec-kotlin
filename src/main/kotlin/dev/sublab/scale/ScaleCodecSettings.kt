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

import dev.sublab.scale.dataContainers.ScaleByteArrayDataContainer
import dev.sublab.scale.dataContainers.ScaleHexDataContainer
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider

/**
 * Scale codec's settings. Contains a data container and an adapter provider
 * @param dataContainer scale data container
 * @param adapterProvider scale codec adapter provider
 */
data class ScaleCodecSettings<Data: Any>(
    val dataContainer: ScaleDataContainer<Data>,
    val adapterProvider: ScaleCodecAdapterProvider
) {
    companion object {
        /**
         * Scale codec's default settings with a scale byte array data container
         * and the default adapter provider
         */
        fun default() = ScaleCodecSettings(
            dataContainer = ScaleByteArrayDataContainer(),
            adapterProvider = DefaultScaleCodecAdapterProvider()
        )

        /**
         * Scale codec's hex settings with a hex data container and the default adapter provider
         */
        fun hex() = ScaleCodecSettings(
            dataContainer = ScaleHexDataContainer(),
            adapterProvider = DefaultScaleCodecAdapterProvider()
        )
    }
}