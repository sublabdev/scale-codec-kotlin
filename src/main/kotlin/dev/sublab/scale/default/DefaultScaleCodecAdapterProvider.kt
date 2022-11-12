package dev.sublab.scale.default

import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterFactory
import dev.sublab.scale.ScaleCodecAdapterProvider
import dev.sublab.scale.adapters.*
import dev.sublab.scale.annotations.EnumClass
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