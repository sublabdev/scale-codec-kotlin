package dev.sublab.scale

import dev.sublab.scale.dataContainers.ScaleByteArrayDataContainer

data class ScaleCodecSettings<Data: Any>(
    val dataContainer: ScaleDataContainer<Data>,
    val adapterProvider: ScaleCodecAdapterProvider
) {
    companion object {
        fun default() = ScaleCodecSettings(
            dataContainer = ScaleByteArrayDataContainer(),
            adapterProvider = DefaultScaleCodecAdapterProvider()
        )
    }
}