package dev.sublab.scale.adapters

import dev.sublab.scale.ByteArrayReader
import dev.sublab.scale.NoAdapterKnown
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.default.AdapterProvider
import kotlin.reflect.KType

/**
 * A generic adapter provider
 */
internal class GenericAdapterProvider<T>(
    val adapterProviders: List<AdapterProvider>,
    val onMatch: (AdapterProvider) -> Unit
): ScaleCodecAdapter<T>() {

    @Throws(NoAdapterKnown::class)
    override fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation>): T {
        for (provider in adapterProviders) {
            val adapter = provider.get<T>() ?: continue

            val offsetBeforeTry = reader.offset

            runCatching {
                adapter.read(reader, type)
            }.onSuccess {
                onMatch(provider)
                return it
            }.onFailure {
                // Reset offset to position before failed reading
                reader.offset = offsetBeforeTry
            }
        }

        throw NoAdapterKnown(type = type)
    }

    override fun write(obj: T, type: KType, annotations: List<Annotation>): ByteArray {
        for (provider in adapterProviders) {
            val adapter = provider.get<T>() ?: continue
            val byteArray = runCatching { adapter.write(obj, type) }.getOrNull()
            if (byteArray != null) {
                onMatch(provider)
                return byteArray
            }
        }

        throw NoAdapterKnown(type = type)
    }
}