package dev.sublab.scale

import dev.sublab.scale.adapters.LongAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestLong: BaseTest<Long>() {
    override val type = Long::class
    override val adapter = LongAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Long.MIN_VALUE..Long.MAX_VALUE).random() }
}