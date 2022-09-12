package dev.sublab.scale

import dev.sublab.scale.adapters.BigIntegerAdapter
import dev.sublab.scale.support.TestCase
import dev.sublab.scale.helpers.decodeHex
import dev.sublab.scale.helpers.toHex
import org.junit.jupiter.api.Test
import java.math.BigInteger
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

    @Test
    internal fun testBigIntegerAdapterEncoding() {
        val adapter = BigIntegerAdapter(DefaultScaleCodecAdapterProvider())
        for (testCase in predefinedTestCases) {
            val encoded = adapter.write(testCase.value, BigInteger::class)

            if (!encoded.contentEquals(testCase.encoded.decodeHex())) {
                println("Encoded ${testCase.value} to: ${encoded.toHex()}, expected: ${testCase.encoded}")
            }
            assert(encoded.contentEquals(testCase.encoded.decodeHex()))
        }
    }

    @Test
    internal fun testBigIntegerAdapterCoding() {
        val adapter = BigIntegerAdapter(DefaultScaleCodecAdapterProvider())
        for (testValue in predefinedTestCases.map { it.value }) {
            val encoded = adapter.write(testValue, BigInteger::class)
            val decoded = adapter.read(ByteArrayReader(encoded), BigInteger::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testBigIntegerCoding() {
        val codec = ScaleCodec.default()
        for (testValue in predefinedTestCases.map { it.value }) {
            val encoded = codec.toScale(testValue, BigInteger::class)
            val decoded = codec.fromScale(encoded, BigInteger::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }
}