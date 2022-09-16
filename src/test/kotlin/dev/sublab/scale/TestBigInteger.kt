package dev.sublab.scale

import dev.sublab.scale.adapters.BigIntegerAdapter
import dev.sublab.scale.helpers.decodeHex
import dev.sublab.scale.helpers.toHex
import dev.sublab.scale.reflection.createGenericType
import dev.sublab.scale.support.TestCase
import java.math.BigInteger
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestBigInteger {

    private val predefinedTestCases = listOf(
        TestCase(BigInteger("0"), "00"),
        TestCase(BigInteger("1"), "04"),
        TestCase(BigInteger("63"), "fc"),
        TestCase(BigInteger("64"), "0101"),
        TestCase(BigInteger("255"), "fd03"),
        TestCase(BigInteger("511"),"fd07"),
        TestCase(BigInteger("16383"), "fdff"),
        TestCase(BigInteger("16384"), "02000100"),
        TestCase(BigInteger("65535"), "feff0300"),
        TestCase(BigInteger("1073741823"), "feffffff"),
        TestCase(BigInteger("1073741824"), "0300000040"),
        TestCase(BigInteger("4592230960395125066"), "134a01e750bae1ba3f")
    )

    private val testValues = predefinedTestCases.map { it.value }
    private val nullableTestValues = testValues + listOf(null)

    @Test
    internal fun testAdapter() {
        val adapter = BigIntegerAdapter(DefaultScaleCodecAdapterProvider())
        for (testCase in predefinedTestCases) {
            val encoded = adapter.write(testCase.value, BigInteger::class)
            if (!encoded.contentEquals(testCase.encoded.decodeHex())) {
                println("Encoded ${testCase.value} to: ${encoded.toHex()}, expected: ${testCase.encoded}")
            }
            assert(encoded.contentEquals(testCase.encoded.decodeHex()))

            val decoded = adapter.read(ByteArrayReader(encoded), BigInteger::class)
            if (testCase.value != decoded) {
                println("Expected: ${testCase.value}, decoded: $decoded")
            }
            assertEquals(testCase.value, decoded)
        }
    }

    @Test
    internal fun testCoding() {
        val codec = ScaleCodec.default()
        for (testValue in testValues) {
            val encoded = codec.toScale(testValue, BigInteger::class)
            val decoded = codec.fromScale(encoded, BigInteger::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testNullableCoding() {
        val codec = ScaleCodec.default()
        val type = BigInteger::class.createType(nullable = true)
        for (testValue in nullableTestValues) {
            val encoded = codec.toScale(testValue, type)
            val decoded = codec.fromScale<BigInteger?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testListCoding() {
        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(BigInteger::class)
        val encoded = codec.toScale(testValues, type)
        val decoded = codec.fromScale<List<BigInteger>>(encoded, type)

        if (testValues != decoded) {
            println("Expected: $testValues, decoded: $decoded")
        }
        assertEquals(testValues, decoded)
    }

    @Test
    internal fun testListOfNullableCoding() {
        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(BigInteger::class.createType(nullable = true))
        val encoded = codec.toScale(nullableTestValues, type)
        val decoded = codec.fromScale<List<BigInteger?>>(encoded, type)

        if (nullableTestValues != decoded) {
            println("Expected: $nullableTestValues, decoded: $decoded")
        }
        assertEquals(nullableTestValues, decoded)
    }
}