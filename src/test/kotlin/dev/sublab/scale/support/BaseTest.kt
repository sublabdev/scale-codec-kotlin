package dev.sublab.scale.support

import dev.sublab.scale.ScaleCodec
import dev.sublab.scale.ScaleCodecAdapter
import dev.sublab.scale.reflection.createGenericType
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class BaseTest<T> {

    abstract val type: KClass<*>

    abstract val adapter: ScaleCodecAdapter<T>
    open val nullableAdapter: ScaleCodecAdapter<T?>? = null

    abstract val testValues: List<T>

    private val baseType get() = type.createType()
    private val nullableType get() = type.createType(nullable = true)

    private val codec = ScaleCodec.default()

    private val nullableTestValues get() = testValues + listOf(null)

    @Test
    fun testAdapter() {
        val testValues = testValues
        for (testValue in testValues) {
            val encoded = adapter.write(testValue, baseType)
            val decoded = adapter.read(encoded, baseType)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testNullableAdapter() {
        val nullableTestValues = nullableTestValues
        val adapter = nullableAdapter ?: return

        for (testValue in nullableTestValues) {
            val encoded = adapter.write(testValue, nullableType)
            val decoded = adapter.read(encoded, nullableType)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    fun testCoding() {
        val testValues = testValues
        for (testValue in testValues) {
            val encoded = codec.toScale(testValue, baseType)
            val decoded = codec.fromScale<T>(encoded, baseType)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    fun testNullableCoding() {
        val nullableTestValues = nullableTestValues
        for (testValue in nullableTestValues) {
            val encoded = codec.toScale(testValue, nullableType)
            val decoded = codec.fromScale<T?>(encoded, nullableType)

            if (testValue != decoded) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assertEquals(testValue, decoded)
        }
    }

    @Test
    internal fun testListCoding() {
        val testValues = testValues

        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(baseType)
        val encoded = codec.toScale(testValues, type)
        val decoded = codec.fromScale<List<T>>(encoded, type)

        if (testValues != decoded) {
            println("Expected: $testValues, decoded: $decoded")
        }
        assertEquals(testValues, decoded)
    }

    @Test
    internal fun testListOfNullableCoding() {
        val nullableTestValues = nullableTestValues

        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(nullableType)
        val encoded = codec.toScale(nullableTestValues, type)
        val decoded = codec.fromScale<List<T?>>(encoded, type)

        if (nullableTestValues != decoded) {
            println("Expected: $nullableTestValues, decoded: $decoded")
        }
        assertEquals(nullableTestValues, decoded)
    }
}