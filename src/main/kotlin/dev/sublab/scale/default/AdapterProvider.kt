package dev.sublab.scale.default

import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterFactory

internal data class AdapterProvider(
    val instance: ScaleCodecAdapter<*>? = null,
    val factory: ScaleCodecAdapterFactory? = null
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(): ScaleCodecAdapter<T>? = (instance as ScaleCodecAdapter<T>?) ?: factory?.make()
}