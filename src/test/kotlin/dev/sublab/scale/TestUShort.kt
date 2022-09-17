package dev.sublab.scale

import dev.sublab.scale.adapters.UShortAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestUShort: BaseTest<UShort>() {
    override val type = UShort::class
    override val adapter = UShortAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UShort.MIN_VALUE..UShort.MAX_VALUE).random().toUShort() }
}