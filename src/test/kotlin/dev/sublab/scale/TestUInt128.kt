package dev.sublab.scale

import dev.sublab.common.numerics.UInt128
import dev.sublab.scale.adapters.UInt128Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestUInt128: BaseTest<UInt128>() {
    override val type = UInt128::class
    override val adapter = UInt128Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(128, Random()) }
            .map { UInt128(it) }
}