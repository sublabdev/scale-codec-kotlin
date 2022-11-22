package dev.sublab.scale

import dev.sublab.common.numerics.Int256
import dev.sublab.scale.adapters.Int256Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestInt256: BaseTest<Int256>() {
    override val type = Int256::class
    override val adapter = Int256Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(128, Random()) }
            .map { Int256(it) }
}