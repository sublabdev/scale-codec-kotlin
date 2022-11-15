package dev.sublab.scale

import dev.sublab.scale.adapters.Int512Adapter
import dev.sublab.scale.dataTypes.Int512
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestInt512: BaseTest<Int512>() {
    override val type = Int512::class
    override val adapter = Int512Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(128, Random()) }
            .map { Int512(it) }
}