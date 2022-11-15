package dev.sublab.scale

import dev.sublab.scale.adapters.Int8Adapter
import dev.sublab.scale.dataTypes.Int8
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestInt8: BaseTest<Int8>() {
    override val type = Int8::class
    override val adapter = Int8Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Int8.MIN_VALUE..Int8.MAX_VALUE).random().toByte() }
}