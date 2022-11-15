package dev.sublab.scale

import dev.sublab.scale.adapters.UInt64Adapter
import dev.sublab.scale.dataTypes.UInt64
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestUInt64: BaseTest<UInt64>() {
    override val type = UInt64::class
    override val adapter = UInt64Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UInt64.MIN_VALUE..UInt64.MAX_VALUE).random() }
}