package dev.sublab.scale

import dev.sublab.common.numerics.UInt512
import dev.sublab.scale.adapters.UInt512Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestUInt512: BaseTest<UInt512>() {
    override val type = UInt512::class
    override val adapter = UInt512Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(512, Random()) }
            .map { UInt512(it) }
}