package dev.sublab.scale

import dev.sublab.common.numerics.Int128
import dev.sublab.scale.adapters.Int128Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestInt128: BaseTest<Int128>() {
    override val type = Int128::class
    override val adapter = Int128Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(128, Random()) }
            .map { Int128(it) }
}