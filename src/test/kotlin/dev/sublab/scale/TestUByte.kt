package dev.sublab.scale

import dev.sublab.scale.adapters.UByteAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

class TestUByte: BaseTest<UByte>() {
    override val type = UByte::class
    override val adapter = UByteAdapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UByte.MIN_VALUE..UByte.MAX_VALUE).random().toUByte() }
}