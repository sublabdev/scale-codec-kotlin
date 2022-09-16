package dev.sublab.scale

import dev.sublab.scale.adapters.EnumAdapter
import dev.sublab.scale.reflection.createGenericType
import java.math.BigInteger
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestEnums {
    private val testValues get() = listOf(
        TestEnum.Bool(true),
        TestEnum.I8(120.toByte()),
        TestEnum.I16(1024),
        TestEnum.I32(-22048),
        TestEnum.I64(-234234),
        TestEnum.U8(250.toUByte()),
        TestEnum.U16(512u),
        TestEnum.U32(22048u),
        TestEnum.U64(234234u),
        TestEnum.Str("string"),
        TestEnum.BigInt(BigInteger.valueOf(253521)),
        TestEnum.Another(TestEnum.Bool(false))
    )

    private val nullableTestValues = testValues + listOf(null)

    @Test
    internal fun testAdapter() {
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
    internal fun testCoding() {
        val codec = ScaleCodec.default()

        for (testValue in testValues) {
            val encoded = codec.toScale(testValue, TestEnum::class)
            val decoded = codec.fromScale(encoded, TestEnum::class)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testNullableCoding() {
        val codec = ScaleCodec.default()
        val type = TestEnum::class.createType(nullable = true)

        for (testValue in nullableTestValues) {
            val encoded = codec.toScale(testValue, type)
            val decoded = codec.fromScale<TestEnum?>(encoded, type)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testListCoding() {
        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(TestEnum::class)
        val encoded = codec.toScale(testValues, type)
        val decoded = codec.fromScale<List<TestEnum>>(encoded, type)

        if (testValues != decoded) {
            println("Expected: $testValues, decoded: $decoded")
        }
        assertEquals(testValues, decoded)
    }

    @Test
    internal fun testListOfNullableCoding() {
        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(TestEnum::class.createType(nullable = true))
        val encoded = codec.toScale(nullableTestValues, type)
        val decoded = codec.fromScale<List<TestEnum?>>(encoded, type)

        if (nullableTestValues != decoded) {
            println("Expected: $nullableTestValues, decoded: $decoded")
        }
        assertEquals(nullableTestValues, decoded)
    }
}

sealed class TestEnum {
    data class Bool(val value: Boolean): TestEnum()
    data class I8(val value: Byte): TestEnum()
    data class I16(val value: Short): TestEnum()
    data class I32(val value: Int): TestEnum()
    data class I64(val value: Long): TestEnum()
    data class U8(val value: UByte): TestEnum()
    data class U16(val value: UShort): TestEnum()
    data class U32(val value: UInt): TestEnum()
    data class U64(val value: ULong): TestEnum()
    data class Str(val value: String): TestEnum()
    data class BigInt(val value: BigInteger): TestEnum()
    data class Another(val value: TestEnum): TestEnum()
}