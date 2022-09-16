package dev.sublab.scale

import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestUnsignedNumbers {
    @Test
    internal fun testUByteCoding() {
        val testValues = (0 until TEST_REPEATS).map { (UByte.MIN_VALUE..UByte.MAX_VALUE).random().toUByte() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, UByte::class)
            val decoded = scaleCodec.fromScale(encoded, UByte::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testUShortCoding() {
        val testValues = (0 until TEST_REPEATS).map { (UShort.MIN_VALUE..UShort.MAX_VALUE).random().toUShort() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, UShort::class)
            val decoded = scaleCodec.fromScale(encoded, UShort::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testOneUIntCoding() {
        val testValue = 134578345u
        val scaleCodec = ScaleCodec.default()

        val encodedValue = scaleCodec.toScale(testValue, UInt::class)
//        println("Encoded $testValue to ${encodedValue.toHex()}")
        val decoded = scaleCodec.fromScale(encodedValue, UInt::class)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assertEquals(testValue, decoded)
    }

    @Test
    internal fun testUIntCoding() {
        val testValues = (0 until TEST_REPEATS).map { (UInt.MIN_VALUE..UInt.MAX_VALUE).random() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, UInt::class)
            val decoded = scaleCodec.fromScale(encoded, UInt::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testULongCoding() {
        val testValues = (0 until TEST_REPEATS).map { (ULong.MIN_VALUE..ULong.MAX_VALUE).random() }
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, ULong::class)
            val decoded = scaleCodec.fromScale(encoded, ULong::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }
}