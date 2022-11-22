package dev.sublab.scale

import dev.sublab.common.numerics.UInt256
import dev.sublab.scale.adapters.UInt256Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestUInt256: BaseTest<UInt256>() {
    override val type = UInt256::class
    override val adapter = UInt256Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(256, Random()) }
            .map { UInt256(it) }
}