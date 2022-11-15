package dev.sublab.scale

import dev.sublab.scale.adapters.UInt16Adapter
import dev.sublab.scale.dataTypes.UInt16
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestUInt16: BaseTest<UInt16>() {
    override val type = UInt16::class
    override val adapter = UInt16Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UInt16.MIN_VALUE..UInt16.MAX_VALUE).random().toUShort() }
}