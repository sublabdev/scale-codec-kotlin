package dev.sublab.scale

import dev.sublab.scale.adapters.UIntAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import kotlin.test.Test
import kotlin.test.assertEquals

class TestUInt: BaseTest<UInt>() {
    override val type = UInt::class
    override val adapter = UIntAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UInt.MIN_VALUE..UInt.MAX_VALUE).random() }

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
}