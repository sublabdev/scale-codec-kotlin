package dev.sublab.scale

import dev.sublab.scale.default.AdapterProvider
import dev.sublab.scale.default.ConditionalAdapterProvider
import dev.sublab.scale.adapters.GenericAdapterProvider
import dev.sublab.scale.reflection.TypeHolder
import dev.sublab.scale.reflection.nullableCreateType
import kotlin.reflect.KClass
import kotlin.reflect.KType

abstract class ScaleCodecAdapterProvider {
    private var nullableAdapter: AdapterProvider? = null
    private var nullableAdapters: MutableMap<TypeHolder, AdapterProvider> = mutableMapOf()
    private var adapters: MutableMap<TypeHolder, AdapterProvider> = mutableMapOf()
    private var conditionalAdapters: MutableList<ConditionalAdapterProvider> = mutableListOf()
    private var genericAdapters: MutableList<AdapterProvider> = mutableListOf()

    // Find

    private var matchTypesCache: MutableMap<KType, AdapterProvider> = mutableMapOf()
    private var matchClassesCache: MutableMap<KClass<*>, AdapterProvider> = mutableMapOf()

    private fun findAdapterProvider(type: KType): AdapterProvider? {
        if (matchTypesCache.containsKey(type)) {
            return matchTypesCache[type]
        }

        if (type.isMarkedNullable) {
            for (entry in nullableAdapters) {
                if (entry.key.conformsTo(type)) {
                    matchTypesCache[type] = entry.value
                    return entry.value
                }
            }

            return nullableAdapter?.let {
                matchTypesCache[type] = it
                it
            } ?: throw NoNullableAdapterException()
        }

        findConditionalProvider(type)?.let {
            matchTypesCache[type] = it
            return it
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

        findConditionalProvider(type)?.let {
            matchClassesCache[type] = it
            return it
        }

        for (entry in adapters) {
            if (entry.key.conformsTo(type)) {
                matchClassesCache[type] = entry.value
                return entry.value
            }
        }

        return null
    }

    private fun findConditionalProvider(type: KType) = conditionalAdapters.firstNotNullOfOrNull {
        it(type)
    }

    private fun findConditionalProvider(type: KClass<*>) = conditionalAdapters.firstNotNullOfOrNull {
        type.nullableCreateType()?.let { kType -> it(kType) }
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

    fun setNullableAdapter(factory: ScaleCodecAdapterFactory) {
        nullableAdapter = AdapterProvider(factory = factory)
    }

    fun <T> setNullableAdapter(adapter: ScaleCodecAdapter<T>, type: KType) {
        nullableAdapters[TypeHolder(type = type)] = AdapterProvider(instance = adapter)
    }

    fun <T> addConditionalAdapter(adapter: ScaleCodecAdapter<T>, condition: (KType) -> Boolean) {
        conditionalAdapters.add {
            if (condition(it)) AdapterProvider(instance = adapter) else null
        }
    }

    fun addConditionalAdapter(factory: ScaleCodecAdapterFactory, condition: (KType) -> Boolean) {
        conditionalAdapters.add {
            if (condition(it)) AdapterProvider(factory = factory) else null
        }
    }

    fun addGenericAdapter(factory: ScaleCodecAdapterFactory) {
        genericAdapters.add(AdapterProvider(factory = factory))
    }
}