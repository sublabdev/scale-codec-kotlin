package dev.sublab.scale

import dev.sublab.scale.adapters.EnumAdapter
import dev.sublab.scale.support.BaseTest
import java.math.BigInteger

internal class TestEnums: BaseTest<TestEnum>() {
    override val type = TestEnum::class
    override val adapter = EnumAdapter<TestEnum>(DefaultScaleCodecAdapterProvider())

    override val testValues get() = listOf(
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

internal class TestBasicEnums: BaseTest<TestBasicEnum>() {
    override val type = TestBasicEnum::class
    override val adapter = EnumAdapter<TestBasicEnum>(DefaultScaleCodecAdapterProvider())

    override val testValues get() = listOf(
        TestBasicEnum.One,
        TestBasicEnum.Two,
        TestBasicEnum.Three
    )
}

enum class TestBasicEnum {
    One,
    Two,
    Three
}