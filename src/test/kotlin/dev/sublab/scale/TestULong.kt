package dev.sublab.scale

import dev.sublab.scale.adapters.ULongAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestULong: BaseTest<ULong>() {
    override val type = ULong::class
    override val adapter = ULongAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (ULong.MIN_VALUE..ULong.MAX_VALUE).random() }
}