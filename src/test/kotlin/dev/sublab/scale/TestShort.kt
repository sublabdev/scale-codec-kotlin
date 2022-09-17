package dev.sublab.scale

import dev.sublab.scale.adapters.ShortAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestShort: BaseTest<Short>() {
    override val type = Short::class
    override val adapter = ShortAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Short.MIN_VALUE..Short.MAX_VALUE).random().toShort() }
}