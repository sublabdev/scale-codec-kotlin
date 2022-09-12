package dev.sublab.scale

import dev.sublab.scale.adapters.*
import dev.sublab.scale.reflection.createFromType
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class NoAdapterKnown(val type: KType? = null): Throwable()
class NoOptionalAdapterException: Throwable()

open class ScaleCodecReadException(reason: String?): Throwable(reason)
open class ScaleCodecWriteException(reason: String?): Throwable(reason)

abstract class ScaleCodecAdapter<T> {
    @Throws(ScaleCodecReadException::class)
    fun read(byteArray: ByteArray, type: KType): T = read(ByteArrayReader(byteArray), type)

    // Implement in adapters

    @Throws(ScaleCodecReadException::class)
    abstract fun read(reader: ByteArrayReader, type: KType): T

    @Throws(ScaleCodecWriteException::class)
    abstract fun write(obj: T, type: KType): ByteArray
}

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(byteArray: ByteArray, type: KClass<T>) = read(byteArray, type.createType())

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(reader: ByteArrayReader, type: KClass<T>) = read(reader, type.createType())

@Throws(ScaleCodecWriteException::class)
fun <T: Any> ScaleCodecAdapter<T>.write(obj: T, type: KClass<T>) = write(obj, type.createType())

data class TypeHolder(
    val type: KType? = null,
    val clazz: KClass<*>? = null
) {
    fun conformsTo(type: KType) = this.type?.equals(type)
        ?: clazz?.createFromType(type)?.equals(type)
        ?: false

    fun conformsTo(type: KClass<*>) = clazz?.equals(type)
        ?: this.type?.equals(type.createType())
        ?: false
}

interface ScaleCodecAdapterFactory {
    fun <T> make(): ScaleCodecAdapter<T>
}

private data class AdapterProvider(
    val instance: ScaleCodecAdapter<*>? = null,
    val factory: ScaleCodecAdapterFactory? = null
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(): ScaleCodecAdapter<T>? = (instance as ScaleCodecAdapter<T>?) ?: factory?.make()
}

private class GenericAdapterProvider<T>(
    val adapterProviders: List<AdapterProvider>,
    val onMatch: (AdapterProvider) -> Unit
): ScaleCodecAdapter<T>() {

    @Throws(NoAdapterKnown::class)
    override fun read(reader: ByteArrayReader, type: KType): T {
        for (provider in adapterProviders) {
            val adapter = provider.get<T>() ?: continue

            val offsetBeforeTry = reader.offset
            val obj = kotlin.runCatching { adapter.read(reader, type) }.getOrNull()
            if (obj != null) {
                onMatch(provider)
                return obj
            } else {
                // Reset offset to position before failed reading
                reader.offset = offsetBeforeTry
            }
        }

        throw NoAdapterKnown(type = type)
    }

    override fun write(obj: T, type: KType): ByteArray {
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

abstract class ScaleCodecAdapterProvider {
    private var optionalAdapter: AdapterProvider? = null
    private var optionalAdapters: MutableMap<TypeHolder, AdapterProvider> = mutableMapOf()
    private var adapters: MutableMap<TypeHolder, AdapterProvider> = mutableMapOf()
    private var genericAdapters: MutableList<AdapterProvider> = mutableListOf()

    // Find

    private var matchTypesCache: MutableMap<KType, AdapterProvider> = mutableMapOf()
    private var matchClassesCache: MutableMap<KClass<*>, AdapterProvider> = mutableMapOf()

    private fun findAdapterProvider(type: KType): AdapterProvider? {
        if (matchTypesCache.containsKey(type)) {
            return matchTypesCache[type]
        }

        if (type.isMarkedNullable) {
            for (entry in optionalAdapters) {
                if (entry.key.conformsTo(type)) {
                    matchTypesCache[type] = entry.value
                    return entry.value
                }
            }

            if (optionalAdapter != null) {
                return optionalAdapter
            } else {
                throw NoOptionalAdapterException()
            }
        }

        for (entry in adapters) {
            if (entry.key.conformsTo(type)) {
                matchTypesCache[type] = entry.value
                return entry.value
            }
        }

        return null
    }

    private fun findAdapterProvider(type: KClass<*>): AdapterProvider? {
        if (matchClassesCache.containsKey(type)) {
            return matchClassesCache[type]
        }

        for (entry in adapters) {
            if (entry.key.conformsTo(type)) {
                matchClassesCache[type] = entry.value
                return entry.value
            }
        }

        return null
    }

    private fun <T> tryGenericProviders(type: KType) = GenericAdapterProvider<T>(genericAdapters) {
        matchTypesCache[type] = it
    }

    private fun <T: Any> tryGenericProviders(type: KClass<T>) = GenericAdapterProvider<T>(genericAdapters) {
        matchClassesCache[type] = it
    }

    @Throws(NoAdapterKnown::class)
    fun <T> findAdapter(type: KType)
        = findAdapterProvider(type)?.get<T>() ?: tryGenericProviders(type)

    @Throws(NoAdapterKnown::class)
    fun <T: Any> findAdapter(type: KClass<T>)
        = findAdapterProvider(type)?.get() ?: tryGenericProviders(type)

    // Set

    fun <T> setAdapter(adapter: ScaleCodecAdapter<T>, type: KType) {
        adapters[TypeHolder(type = type)] = AdapterProvider(instance = adapter)
    }

    fun <T: Any> setAdapter(adapter: ScaleCodecAdapter<T>, type: KClass<T>) {
        adapters[TypeHolder(clazz = type)] = AdapterProvider(instance = adapter)
    }

    fun <T> setAdapter(factory: ScaleCodecAdapterFactory, type: KType) {
        adapters[TypeHolder(type = type)] = AdapterProvider(factory = factory)
    }

    fun <T: Any> setAdapter(factory: ScaleCodecAdapterFactory, type: KClass<T>) {
        adapters[TypeHolder(clazz = type)] = AdapterProvider(factory = factory)
    }

    fun setOptionalAdapter(factory: ScaleCodecAdapterFactory) {
        optionalAdapter = AdapterProvider(factory = factory)
    }

    fun <T> setOptionalAdapter(adapter: ScaleCodecAdapter<T>, type: KType) {
        optionalAdapters[TypeHolder(type = type)] = AdapterProvider(instance = adapter)
    }

    fun addGenericAdapter(factory: ScaleCodecAdapterFactory) {
        genericAdapters.add(AdapterProvider(factory = factory))
    }
}

class DefaultScaleCodecAdapterProvider : ScaleCodecAdapterProvider() {
    init {
        // Primitives
        provideBoolean()
        provideNumbers()
        provideUNumbers()
        provideList()
        provideString()
        provideBigInteger()

        // Generic
        provideOptional()
        provideEnum()
        provideStruct()
    }

    private val adapterProvider: ScaleCodecAdapterProvider get() = this

    private fun provideBoolean() {
        setAdapter(BooleanAdapter(), Boolean::class)
        setOptionalAdapter(OptionalBooleanAdapter(), Boolean::class.createType(nullable = true))
    }

    private fun provideNumbers() {
        setAdapter(ByteAdapter(), Byte::class)
        setAdapter(ShortAdapter(), Short::class)
        setAdapter(IntAdapter(), Int::class)
        setAdapter(LongAdapter(), Long::class)
    }

    private fun provideUNumbers() {
        setAdapter(UByteAdapter(), UByte::class)
        setAdapter(UShortAdapter(), UShort::class)
        setAdapter(UIntAdapter(), UInt::class)
        setAdapter(ULongAdapter(), ULong::class)
    }

    private fun provideBigInteger() {
        setAdapter(BigIntegerAdapter(adapterProvider), BigInteger::class)
    }

    private fun provideList() {
        setAdapter(object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = ListAdapter<T>(adapterProvider) as ScaleCodecAdapter<T>
        }, List::class)
    }

    private fun provideString() {
        setAdapter(StringAdapter(this), String::class)
    }

    private fun provideOptional() {
        setOptionalAdapter(object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = OptionalAdapter<T>(adapterProvider) as ScaleCodecAdapter<T>
        })
    }

    private fun provideEnum() {
        addGenericAdapter(object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = EnumAdapter<Any>(adapterProvider) as ScaleCodecAdapter<T>
        })
    }

    private fun provideStruct() {
        addGenericAdapter(object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = StructAdapter<Any>(adapterProvider) as ScaleCodecAdapter<T>
        })
    }
}