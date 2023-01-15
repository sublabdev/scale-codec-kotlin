/**
 *
 * Copyright 2023 SUBSTRATE LABORATORY LLC <info@sublab.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package dev.sublab.scale.default

import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.ScaleCodecAdapterFactory
import dev.sublab.scale.ScaleCodecAdapterProvider
import dev.sublab.scale.adapters.*
import dev.sublab.scale.annotations.EnumClass
import dev.sublab.common.numerics.*
import dev.sublab.scale.types.ScaleEncodedByteArray
import java.math.BigInteger
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation

/**
 * Default scale codec adapter provider which provides adapter for the main by default supported types. The adapters for
 * those types are set during the provider's initialization, so no additional step is required for that.
 */
class DefaultScaleCodecAdapterProvider : ScaleCodecAdapterProvider() {
    init {
        // Primitives
        provideBoolean()
        provideNumbers()
        provideUNumbers()
        provideList()
        provideString()
        provideBigInteger()
        provideByteArray()

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

    private fun provideByteArray() {
        setAdapter(ByteArrayAdapter(adapterProvider), ByteArray::class)
        setAdapter(ScaleEncodedByteArrayAdapter(), ScaleEncodedByteArray::class)
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