package dev.sublab.scale

/**
 * An interface for making a ScaleCodecAdapter of a generic type T
 */
interface ScaleCodecAdapterFactory {
    /**
     * Makes a ScaleCodecAdapter of a generic type T
     */
    fun <T> make(): ScaleCodecAdapter<T>
}