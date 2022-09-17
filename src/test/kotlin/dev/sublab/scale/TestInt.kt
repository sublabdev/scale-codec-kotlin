package dev.sublab.scale

import dev.sublab.scale.adapters.IntAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import kotlin.test.Test
import kotlin.test.assertEquals

class TestInt: BaseTest<Int>() {
    override val type = Int::class
    override val adapter = IntAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Int.MIN_VALUE..Int.MAX_VALUE).random() }

    @Test
    internal fun testOneIntCoding() {
        val testValue = -1872042254
        val scaleCodec = ScaleCodec.default()

        val encodedValue = scaleCodec.toScale(testValue, Int::class)
        val decoded = scaleCodec.fromScale(encodedValue, Int::class)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assertEquals(testValue, decoded)
    }
}