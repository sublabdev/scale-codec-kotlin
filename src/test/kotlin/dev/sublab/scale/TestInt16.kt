package dev.sublab.scale

import dev.sublab.common.numerics.Int16
import dev.sublab.scale.adapters.Int16Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestInt16: BaseTest<Int16>() {
    override val type = Int16::class
    override val adapter = Int16Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Int16.MIN_VALUE..Int16.MAX_VALUE).random().toShort() }
}