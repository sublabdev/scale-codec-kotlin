package dev.sublab.scale

import dev.sublab.scale.adapters.StringAdapter
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.util.*

internal class TestString: BaseTest<String>() {
    override val type = String::class
    override val adapter = StringAdapter(DefaultScaleCodecAdapterProvider())
    override val testValues = (1..TEST_REPEATS).map {
        UUID.randomUUID().toString()
    }
}