package dev.sublab.scale

import dev.sublab.hex.hex
import dev.sublab.scale.adapters.BigIntegerAdapter
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TestCase
import java.math.BigInteger
import kotlin.test.Test

internal class TestBigInteger: BaseTest<BigInteger>() {
    override val type = BigInteger::class
    override val adapter = BigIntegerAdapter(DefaultScaleCodecAdapterProvider())

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

    override val testValues = listOf<BigInteger>() //predefinedTestCases.map { it.value }

    @Test
    internal fun testEncoding() {
        for (testCase in predefinedTestCases) {
            val encoded = adapter.write(testCase.value, BigInteger::class)
            if (!encoded.contentEquals(testCase.encoded.hex.decode())) {
                println("Encoded ${testCase.value} to: ${encoded.hex.encode()}, expected: ${testCase.encoded}")
            }
            assert(encoded.contentEquals(testCase.encoded.hex.decode()))
        }
    }
}