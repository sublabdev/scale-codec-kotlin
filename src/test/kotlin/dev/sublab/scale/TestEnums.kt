package dev.sublab.scale

import dev.sublab.scale.adapters.EnumAdapter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestEnums {
    private val testValues get() = listOf(TestEnum.Bool(true), TestEnum.Integer(53))

    @Test
    internal fun testEnumAdapter() {
        val adapter = EnumAdapter<TestEnum>(DefaultScaleCodecAdapterProvider())

        for (testValue in testValues) {
            val encoded = adapter.write(testValue, TestEnum::class)
            val decoded = adapter.read(encoded, TestEnum::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testEnumCoding() {
        val scaleCodec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = scaleCodec.toScale(testValue, TestEnum::class)
            val decoded = scaleCodec.fromScale(encoded, TestEnum::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }
}

sealed class TestEnum {
    data class Bool(val value: Boolean): TestEnum()
    data class Integer(val value: Int): TestEnum()
}