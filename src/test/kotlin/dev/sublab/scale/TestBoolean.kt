package dev.sublab.scale

import dev.sublab.scale.adapters.BooleanAdapter
import dev.sublab.scale.adapters.NullableBooleanAdapter
import dev.sublab.scale.support.BaseTest

internal class TestBoolean: BaseTest<Boolean>() {
    override val type = Boolean::class
    override val adapter = BooleanAdapter()
    override val nullableAdapter = NullableBooleanAdapter()

    override val testValues = listOf(true, false, true, false)
}