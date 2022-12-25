package dev.sublab.scale

import dev.sublab.scale.dataContainers.ScaleByteArrayDataContainer
import dev.sublab.scale.dataContainers.ScaleHexDataContainer
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider

data class ScaleCodecSettings<Data: Any>(
    val dataContainer: ScaleDataContainer<Data>,
    val adapterProvider: ScaleCodecAdapterProvider
) {
    companion object {
        fun default() = ScaleCodecSettings(
            dataContainer = ScaleByteArrayDataContainer(),
            adapterProvider = DefaultScaleCodecAdapterProvider()
        )

        fun hex() = ScaleCodecSettings(
            dataContainer = ScaleHexDataContainer(),
            adapterProvider = DefaultScaleCodecAdapterProvider()
        )
    }
}