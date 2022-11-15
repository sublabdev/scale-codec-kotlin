package dev.sublab.scale.default

import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterFactory
import dev.sublab.scale.ScaleCodecAdapterProvider
import dev.sublab.scale.adapters.*
import dev.sublab.scale.annotations.EnumClass
import dev.sublab.scale.dataTypes.*
import java.math.BigInteger
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation

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
        provideNullable()
        provideEnum()
        provideStruct()
    }

    private val adapterProvider: ScaleCodecAdapterProvider get() = this

    private fun provideBoolean() {
        setAdapter(BooleanAdapter(), Boolean::class)
        setNullableAdapter(NullableBooleanAdapter(), Boolean::class.createType(nullable = true))
    }

    private fun provideNumbers() {
        setAdapter(Int8Adapter(), Byte::class)
        setAdapter(Int8Adapter(), Int8::class)
        setAdapter(Int16Adapter(), Short::class)
        setAdapter(Int16Adapter(), Int16::class)
        setAdapter(Int32Adapter(), Int::class)
        setAdapter(Int32Adapter(), Int32::class)
        setAdapter(Int64Adapter(), Long::class)
        setAdapter(Int64Adapter(), Int64::class)
        setAdapter(Int128Adapter(), Int128::class)
        setAdapter(Int256Adapter(), Int256::class)
        setAdapter(Int512Adapter(), Int512::class)
    }

    private fun provideUNumbers() {
        setAdapter(UInt8Adapter(), UByte::class)
        setAdapter(UInt8Adapter(), UInt8::class)
        setAdapter(UInt16Adapter(), UShort::class)
        setAdapter(UInt16Adapter(), UInt16::class)
        setAdapter(UInt32Adapter(), UInt::class)
        setAdapter(UInt32Adapter(), UInt32::class)
        setAdapter(UInt64Adapter(), ULong::class)
        setAdapter(UInt64Adapter(), UInt64::class)
        setAdapter(UInt128Adapter(), UInt128::class)
        setAdapter(UInt256Adapter(), UInt256::class)
        setAdapter(UInt512Adapter(), UInt512::class)
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

    private fun provideNullable() {
        setNullableAdapter(object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = NullableAdapter<T>(adapterProvider) as ScaleCodecAdapter<T>
        })
    }

    private fun provideEnum() {
        val factory = object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = EnumAdapter<Any>(adapterProvider) as ScaleCodecAdapter<T>
        }

        addConditionalAdapter(factory = factory) { type ->
            type.findAnnotation<EnumClass>() != null
        }

        addGenericAdapter(factory)
    }

    private fun provideStruct() {
        addGenericAdapter(object: ScaleCodecAdapterFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> make() = StructAdapter<Any>(adapterProvider) as ScaleCodecAdapter<T>
        })
    }
}