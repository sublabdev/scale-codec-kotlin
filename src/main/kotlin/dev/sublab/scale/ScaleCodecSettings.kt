package dev.sublab.scale

import dev.sublab.scale.dataContainers.ScaleByteArrayDataContainer
import dev.sublab.scale.dataContainers.ScaleHexDataContainer
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider

/**
 * Scale codec's settings. Contains a data container and an adapter provider
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