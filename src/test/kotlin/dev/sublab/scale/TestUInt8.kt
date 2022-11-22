package dev.sublab.scale

import dev.sublab.common.numerics.UInt8
import dev.sublab.scale.adapters.UInt8Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestUInt8: BaseTest<UInt8>() {
    override val type = UInt8::class
    override val adapter = UInt8Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UInt8.MIN_VALUE..UInt8.MAX_VALUE).random().toUByte() }
}