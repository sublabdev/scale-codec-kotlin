package dev.sublab.scale

import dev.sublab.scale.adapters.StringAdapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS

internal class TestString: BaseTest<String>() {
    override val type = String::class
    override val adapter = StringAdapter(DefaultScaleCodecAdapterProvider())

    private val letters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    private fun createStrings(count: Int = TEST_REPEATS, stringLength: Int = 64)
        = (1..count).map {
            (1..stringLength)
                .map { letters.random() }
                .joinToString("")
        }

    override val testValues = createStrings()
}