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

    open fun equals(lhs: T, rhs: T) = lhs == rhs

    @JvmName("equalsNullable")
    private fun equals(lhs: T?, rhs: T?) =
        if (lhs == null && rhs == null) true
        else if (lhs != null && rhs != null) equals(lhs, rhs)
        else false

    private fun equals(lhs: List<T>, rhs: List<T>): Boolean {
        if (lhs.size != rhs.size) return false
        return lhs
            .mapIndexed { index, left -> equals(rhs[index], left) }
            .fold(true) { result, it -> result && it }
    }

    @JvmName("equalsNullable")
    private fun equals(lhs: List<T?>, rhs: List<T?>): Boolean {
        if (lhs.size != rhs.size) return false
        return lhs
            .mapIndexed { index, left -> equals(rhs[index], left) }
            .fold(true) { result, it -> result && it }
    }

    @Test
    internal fun testComp() {
        assert(equals(null, null))
    }

    @Test
    internal fun testAdapter() {
        val testValues = testValues
        for (testValue in testValues) {
            val encoded = adapter.write(testValue, baseType)
            val decoded = adapter.read(encoded, baseType)

            if (!equals(testValue, decoded)) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(equals(testValue, decoded))
        }
    }

    @Test
    internal fun testNullableAdapter() {
        val nullableTestValues = nullableTestValues
        val adapter = nullableAdapter ?: return

        for (testValue in nullableTestValues) {
            val encoded = adapter.write(testValue, nullableType)
            val decoded = adapter.read(encoded, nullableType)

            if (!equals(testValue, decoded)) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(equals(testValue, decoded))
        }
    }

    @Test
    fun testCoding() {
        val testValues = testValues
        for (testValue in testValues) {
            val encoded = codec.toScale(testValue, baseType)
            val decoded = codec.fromScale<T>(encoded, baseType)

            if (!equals(testValue, decoded)) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(equals(testValue, decoded))
        }
    }

    @Test
    fun testNullableCoding() {
        val nullableTestValues = nullableTestValues
        for (testValue in nullableTestValues) {
            val encoded = codec.toScale(testValue, nullableType)
            val decoded = codec.fromScale<T?>(encoded, nullableType)

            if (!equals(testValue, decoded)) {
                println("Expected: $testValue, decoded: $decoded")
            }
            assert(equals(testValue, decoded))
        }
    }

    @Test
    internal fun testListCoding() {
        val testValues = testValues

        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(baseType)
        val encoded = codec.toScale(testValues, type)
        val decoded = codec.fromScale<List<T>>(encoded, type)

        if (!equals(testValues, decoded)) {
            println("Expected: $testValues, decoded: $decoded")
        }
        assert(equals(testValues, decoded))
    }

    @Test
    internal fun testListOfNullableCoding() {
        val nullableTestValues = nullableTestValues

        val codec = ScaleCodec.default()
        val type = List::class.createGenericType(nullableType)
        val encoded = codec.toScale(nullableTestValues, type)
        val decoded = codec.fromScale<List<T?>>(encoded, type)

        if (!equals(nullableTestValues, decoded)) {
            println("Expected: $nullableTestValues, decoded: $decoded")
        }
        assert(equals(nullableTestValues, decoded))
    }
}