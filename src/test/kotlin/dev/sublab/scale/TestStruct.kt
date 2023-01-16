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

package dev.sublab.scale

import dev.sublab.common.numerics.*
import dev.sublab.scale.adapters.StructAdapter
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider
import java.math.BigInteger
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestStructs {
    @Test
    internal fun testAdapter() {
        // Data classes
        run {
            val testValue = TestDataStruct.make(TestStruct())
            val structAdapter = StructAdapter<TestDataStruct>(DefaultScaleCodecAdapterProvider())
            val encoded = structAdapter.write(testValue, TestDataStruct::class)
            val decoded = structAdapter.read(ByteArrayReader(encoded), TestDataStruct::class)

            if (testValue.compareTo(decoded) != 0) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(testValue.compareTo(decoded) == 0)
        }

        // Regular classes
        run {
            val testValue = TestStruct()
            val structAdapter = StructAdapter<TestStruct>(DefaultScaleCodecAdapterProvider())
            val encoded = structAdapter.write(testValue, TestStruct::class)
            val decoded = structAdapter.read(ByteArrayReader(encoded), TestStruct::class)

            if (testValue.compareTo(decoded) != 0) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(testValue.compareTo(decoded) == 0)
        }
    }

    @Test
    internal fun testCoding() {
        val scaleCodec = ScaleCodec.default()

        // Data classes
        run {
            val testValue = TestDataStruct.make(TestStruct())
            val encoded = scaleCodec.toScale(testValue, TestDataStruct::class)
            val decoded = scaleCodec.fromScale(encoded, TestDataStruct::class)

            if (testValue.compareTo(decoded) != 0) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(testValue.compareTo(decoded) == 0)
        }

        // Regular classes
        run {
            val testValue = TestStruct()
            val encoded = scaleCodec.toScale(testValue, TestStruct::class)
            val decoded = scaleCodec.fromScale(encoded, TestStruct::class)

            if (testValue.compareTo(decoded) != 0) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(testValue.compareTo(decoded) == 0)
        }
    }

    @Test
    internal fun testNullableCoding() {
        val scaleCodec = ScaleCodec.default()

        // Data class Null
        run {
            val testValue: TestDataStruct? = null
            val type = TestDataStruct::class.createType(nullable = true)

            val encoded = scaleCodec.toScale(testValue, type)
            val decoded = scaleCodec.fromScale<TestDataStruct?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }

        // Data classes
        run {
            val testValue: TestDataStruct? = TestDataStruct.make(TestStruct())
            val type = TestDataStruct::class.createType(nullable = true)

            val encoded = scaleCodec.toScale(testValue, type)
            val decoded = scaleCodec.fromScale<TestDataStruct?>(encoded, type)

            fun equals(lhs: TestDataStruct?, rhs: TestDataStruct?): Boolean {
                return if (lhs != null && rhs != null) {
                    lhs.compareTo(rhs) == 0
                } else lhs == null && rhs == null
            }

            if (!equals(testValue, decoded)) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(equals(testValue, decoded))
        }

        // Regular class Null
        run {
            val testValue: TestStruct? = null
            val type = TestStruct::class.createType(nullable = true)

            val encoded = scaleCodec.toScale(testValue, type)
            val decoded = scaleCodec.fromScale<TestStruct?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }

        // Regular classes
        run {
            val testValue: TestStruct? = TestStruct()
            val type = TestStruct::class.createType(nullable = true)

            val encoded = scaleCodec.toScale(testValue, type)
            val decoded = scaleCodec.fromScale<TestStruct?>(encoded, type)

            fun equals(lhs: TestStruct?, rhs: TestStruct?): Boolean {
                return if (lhs != null && rhs != null) {
                    lhs.compareTo(rhs) == 0
                } else lhs == null && rhs == null
            }

            if (!equals(testValue, decoded)) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(equals(testValue, decoded))
        }
    }
}

data class TestDataStruct(
    val bool: Boolean,
    val i8: Int8,
    val i16: Int16,
    val i32: Int32,
    val i64: Int64,
    val u8: UInt8,
    val u16: UInt16,
    val u32: UInt32,
    val u64: UInt64,
    val str: String,
    val bigInt: BigInteger
): Comparable<TestDataStruct> {
    companion object {
        fun make(testStruct: TestStruct) = TestDataStruct(
            testStruct.bool,
            testStruct.i8,
            testStruct.i16,
            testStruct.i32,
            testStruct.i64,
            testStruct.u8,
            testStruct.u16,
            testStruct.u32,
            testStruct.u64,
            testStruct.str,
            testStruct.bigInt
        )
    }

    override fun compareTo(other: TestDataStruct) = compareValuesBy(
        this, other,
        { it.bool },
        { it.i8 },
        { it.i16 },
        { it.i32 },
        { it.i64 },
        { it.u8 },
        { it.u16 },
        { it.u32 },
        { it.u64 },
        { it.i8 },
        { it.i16 },
        { it.i32 },
        { it.i64 },
        { it.str },
        { it.bigInt }
    )
}

class TestStruct: Comparable<TestStruct> {
    val bool: Boolean = true
    val i8: Int8 = 120.toByte()
    val i16: Int16 = 1024
    val i32: Int32 = -22048
    val i64: Int64 = -234234
    val u8: UInt8 = 250.toUByte()
    val u16: UInt16 = 512u
    val u32: UInt32 = 22048u
    val u64: UInt64 = 234234u
    val str: String = "string"
    val bigInt: BigInteger = BigInteger.valueOf(253521)

    override fun compareTo(other: TestStruct) = compareValuesBy(
        this, other,
        { it.bool },
        { it.i8 },
        { it.i16 },
        { it.i32 },
        { it.i64 },
        { it.u8 },
        { it.u16 },
        { it.u32 },
        { it.u64 },
        { it.i8 },
        { it.i16 },
        { it.i32 },
        { it.i64 },
        { it.str },
        { it.bigInt }
    )
}