package dev.sublab.scale

import dev.sublab.scale.reflection.createGenericType
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType
import kotlin.test.assertEquals

internal class TestStrings {

    private val letters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    private fun createStrings(count: Int = TEST_REPEATS, stringLength: Int = 64, includeNulls: Boolean = false)
        = (1..count).map {
            if (includeNulls && it %2 == 0) {
                return@map null
            }

            (1..stringLength)
                .map { letters.random() }
                .joinToString("")
        }

    @Test
    internal fun testStringCoding() {
        val testValues = createStrings().mapNotNull { it }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, String::class)
            val decoded = scaleCodec.fromScale(encoded, String::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(testValue == decoded)
        }
    }

    @Test
    internal fun testOptionalStringCoding() {
        val testValues = createStrings(includeNulls = true)
        val scaleCodec = ScaleCodec.default()
        val type = String::class.createType(nullable = true)

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, type)
            val decoded = scaleCodec.fromScale<String?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(testValue == decoded)
        }
    }

    @Test
    internal fun testListOfStringsCoding() {
        val testValue = createStrings().mapNotNull { it }
        val scaleCodec = ScaleCodec.default()
        val type = List::class.createGenericType(String::class)

        val encodedValue = scaleCodec.toScale(testValue, type)
        val decoded = scaleCodec.fromScale<List<String>>(encodedValue, type)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assert(testValue == decoded)
    }
}