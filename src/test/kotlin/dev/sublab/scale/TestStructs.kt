package dev.sublab.scale

import dev.sublab.scale.adapters.StructAdapter
import dev.sublab.scale.helpers.toHex
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType
import kotlin.test.assertEquals

internal class TestStructs {
    @Test
    internal fun testStructAdapter() {
        // Data classes
        run {
            val testValue = TestDataStruct(53, true, 256)
            val structAdapter = StructAdapter<TestDataStruct>(DefaultScaleCodecAdapterProvider())
            val encoded = structAdapter.write(testValue, TestDataStruct::class)
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = structAdapter.read(ByteArrayReader(encoded), TestDataStruct::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }

        // Regular classes
        run {
            val testValue = TestStruct()
            val structAdapter = StructAdapter<TestStruct>(DefaultScaleCodecAdapterProvider())
            val encoded = structAdapter.write(testValue, TestStruct::class)
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = structAdapter.read(ByteArrayReader(encoded), TestStruct::class)

            if (testValue.int != decoded.int || testValue.bool != decoded.bool || testValue.long != decoded.long) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue.int, decoded.int)
            assertEquals(testValue.bool, decoded.bool)
            assertEquals(testValue.long, decoded.long)
        }
    }

    @Test
    internal fun testStructCoding() {
        val scaleCodec = ScaleCodec.default()

        // Data classes
        run {
            val testValue = TestDataStruct(53, true, 256)
            val encoded = scaleCodec.toScale(testValue, TestDataStruct::class)
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = scaleCodec.fromScale(encoded, TestDataStruct::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }

        // Regular classes
        run {
            val testValue = TestStruct()
            val encoded = scaleCodec.toScale(testValue, TestStruct::class)
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = scaleCodec.fromScale(encoded, TestStruct::class)

            if (testValue.int != decoded.int || testValue.bool != decoded.bool || testValue.long != decoded.long) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue.int, decoded.int)
            assertEquals(testValue.bool, decoded.bool)
            assertEquals(testValue.long, decoded.long)
        }
    }

    @Test
    internal fun testOptionalStructCoding() {
        val scaleCodec = ScaleCodec.default()

        // Null
        run {
            val testValue: TestDataStruct? = null
            val type = TestDataStruct::class.createType(nullable = true)

            val encoded = scaleCodec.toScale(testValue, type)
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = scaleCodec.fromScale<TestDataStruct?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }

        // Data classes
        run {
            val testValue: TestDataStruct? = TestDataStruct(53, true, 256)
            val type = TestDataStruct::class.createType(nullable = true)

            val encoded = scaleCodec.toScale(testValue, type)
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = scaleCodec.fromScale<TestDataStruct?>(encoded, type)

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
//            println("Encoded $testValue to ${encoded.toHex()}")
            val decoded = scaleCodec.fromScale<TestStruct?>(encoded, type)

            if (testValue?.int != decoded?.int || testValue?.bool != decoded?.bool || testValue?.long != decoded?.long) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue?.int, decoded?.int)
            assertEquals(testValue?.bool, decoded?.bool)
            assertEquals(testValue?.long, decoded?.long)
        }
    }
}

data class TestDataStruct(
    val int: Int,
    val bool: Boolean,
    val long: Long
)

class TestStruct {
    val int: Int = 53
    val bool: Boolean = true
    val long: Long = 256
}