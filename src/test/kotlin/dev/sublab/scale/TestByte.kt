package dev.sublab.scale

import dev.sublab.scale.adapters.ByteAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestByte: BaseTest<Byte>() {
    override val type = Byte::class
    override val adapter = ByteAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (Byte.MIN_VALUE..Byte.MAX_VALUE).random().toByte() }
}