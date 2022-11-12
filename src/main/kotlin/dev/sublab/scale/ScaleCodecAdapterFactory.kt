package dev.sublab.scale

interface ScaleCodecAdapterFactory {
    fun <T> make(): ScaleCodecAdapter<T>
}