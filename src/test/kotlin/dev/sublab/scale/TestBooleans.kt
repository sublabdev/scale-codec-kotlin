package dev.sublab.scale

import dev.sublab.scale.adapters.BooleanAdapter
import dev.sublab.scale.adapters.NullableBooleanAdapter
import dev.sublab.scale.reflection.createGenericType
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestBooleans {

    private val testValues = listOf(true, false, true, false)
    private val nullableTestValues = testValues + listOf(null)

    @Test
    internal fun testAdapter() {
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
    internal fun testCoding() {
        val codec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = codec.toScale(testValue, Boolean::class)
            val decoded = codec.fromScale(encoded, Boolean::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }
    @Test
    internal fun testNullableAdapter() {
        val adapter = NullableBooleanAdapter()
        val type = Boolean::class.createType(nullable = true)

        for (testValue in nullableTestValues) {
            val encoded = adapter.write(testValue, type)
            val decoded = adapter.read(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testNullableCoding() {
        val codec = ScaleCodec.default()
        val type = Boolean::class.createType(nullable = true)

        for (testValue in testValues) {
            val encoded = codec.toScale(testValue, type)
            val decoded = codec.fromScale<Boolean?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testListCoding() {
        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(Boolean::class)

        val encodedValue = codec.toScale(testValues, type)
        val decoded = codec.fromScale<List<Boolean>>(encodedValue, type)

        if (testValues != decoded) {
            println("Expected: $testValues, decoded: $decoded")
        }
        assertEquals(testValues, decoded)
    }

    @Test
    internal fun testListOfNullableCoding() {
        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(Boolean::class.createType(nullable = true))

        val encodedValue = codec.toScale(nullableTestValues, type)
        val decoded = codec.fromScale<List<Boolean>>(encodedValue, type)

        if (nullableTestValues != decoded) {
            println("Expected: $nullableTestValues, decoded: $decoded")
        }
        assertEquals(nullableTestValues, decoded)
    }
}