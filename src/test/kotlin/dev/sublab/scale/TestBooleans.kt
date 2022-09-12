package dev.sublab.scale

import dev.sublab.scale.adapters.BooleanAdapter
import dev.sublab.scale.adapters.OptionalBooleanAdapter
import dev.sublab.scale.reflection.createGenericType
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType
import kotlin.test.assertEquals

internal class TestBooleans {
    @Test
    internal fun testBooleanAdapter() {
        val testValues = listOf(true, false, true, false)
        val adapter = BooleanAdapter()

        for (testValue in testValues) {
            val encoded = adapter.write(testValue, Boolean::class)
            val decoded = adapter.read(encoded, Boolean::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testBooleanCoding() {
        val testValues = listOf(true, false, true, false)
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, Boolean::class)
            val decoded = scaleCodec.fromScale(encoded, Boolean::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }
    @Test
    internal fun testOptionalBooleanAdapter() {
        val testValues = listOf(true, false, null)
        val adapter = OptionalBooleanAdapter()
        val type = Boolean::class.createType(nullable = true)

        for (testValue in testValues) {
            val encoded = adapter.write(testValue, type)
            val decoded = adapter.read(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testOptionalBooleanCoding() {
        val testValues = listOf(true, false, null)
        val scaleCodec = ScaleCodec.default()
        val type = Boolean::class.createType(nullable = true)

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, type)
            val decoded = scaleCodec.fromScale<Boolean?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testListOfBooleansCoding() {
        val testValue = listOf(true, false, false, true, false)
        val scaleCodec = ScaleCodec.default()
        val type = List::class.createGenericType(Boolean::class)

        val encodedValue = scaleCodec.toScale(testValue, type)
        val decoded = scaleCodec.fromScale<List<Boolean>>(encodedValue, type)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assertEquals(testValue, decoded)
    }
}