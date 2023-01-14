package dev.sublab.scale.default

import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterFactory

/**
 * Handles providing or creating of a `ScaleCodecAdapter` object
 */
internal data class AdapterProvider(
    val instance: ScaleCodecAdapter<*>? = null,
    val factory: ScaleCodecAdapterFactory? = null
) {
    /**
     * Creates or provides an existing (under it's `instance` property) adapter
     * @return An adapter either created or cached
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(): ScaleCodecAdapter<T>? = (instance as ScaleCodecAdapter<T>?) ?: factory?.make()
}