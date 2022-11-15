package dev.sublab.scale

import dev.sublab.scale.adapters.Int32Adapter
import dev.sublab.scale.dataTypes.Int32
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import kotlin.test.Test
import kotlin.test.assertEquals

class TestInt32: BaseTest<Int32>() {
    override val type = Int32::class
    override val adapter = Int32Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Int32.MIN_VALUE..Int32.MAX_VALUE).random() }

    @Test
    internal fun testOneIntCoding() {
        val testValue = -1872042254
        val scaleCodec = ScaleCodec.default()

        val encodedValue = scaleCodec.toScale(testValue, Int32::class)
        val decoded = scaleCodec.fromScale(encodedValue, Int32::class)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assertEquals(testValue, decoded)
    }
}