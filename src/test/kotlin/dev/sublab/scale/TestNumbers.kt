package dev.sublab.scale

import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestNumbers {
    @Test
    internal fun testByteCoding() {
        val testValues = (0 until TEST_REPEATS).map { (Byte.MIN_VALUE..Byte.MAX_VALUE).random().toByte() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, Byte::class)
            val decoded = scaleCodec.fromScale(encoded, Byte::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testShortCoding() {
        val testValues = (0 until TEST_REPEATS).map { (Short.MIN_VALUE..Short.MAX_VALUE).random().toShort() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, Short::class)
            val decoded = scaleCodec.fromScale(encoded, Short::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testOneIntCoding() {
        val testValue = -1934585256
        val scaleCodec = ScaleCodec.default()

        val encodedValue = scaleCodec.toScale(testValue, Int::class)
//        println("Encoded $testValue to ${encodedValue.toHex()}")
        val decoded = scaleCodec.fromScale(encodedValue, Int::class)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assertEquals(testValue, decoded)
    }

    @Test
    internal fun testIntCoding() {
        val testValues = (0 until TEST_REPEATS).map { (Int.MIN_VALUE..Int.MAX_VALUE).random() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, Int::class)
            val decoded = scaleCodec.fromScale(encoded, Int::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testLongCoding() {
        val testValues = (0 until TEST_REPEATS).map { (Long.MIN_VALUE..Long.MAX_VALUE).random() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, Long::class)
            val decoded = scaleCodec.fromScale(encoded, Long::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }
}