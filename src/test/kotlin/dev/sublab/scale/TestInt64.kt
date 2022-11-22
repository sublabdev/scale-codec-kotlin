package dev.sublab.scale

import dev.sublab.common.numerics.Int64
import dev.sublab.scale.adapters.Int64Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestInt64: BaseTest<Int64>() {
    override val type = Int64::class
    override val adapter = Int64Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Int64.MIN_VALUE..Int64.MAX_VALUE).random() }
}