package dev.sublab.scale

import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Handles Scale Codec transaction for generic types. Provides a mechanism for encoding and appending multiple
 * values and decode them.
 */
class ScaleCodecTransaction<Data: Any>(private val codec: ScaleCodec<Data>) {
    private var byteArray = byteArrayOf()

    /**
     * Appends additional generic objects to existing encoded data
     * @return Returns `self` with updated encoded data
     */
    @Throws
    fun <T> append(obj: T, type: KType) = apply {
        byteArray += codec.fromData(codec.toScale(obj, type))
    }

    @Throws
    fun <T: Any> append(obj: T, type: KClass<T>) = apply {
        byteArray += codec.fromData(codec.toScale(obj, type))
    }

    /**
     * Decodes the existing encoded data
     * @return An accumulated data of previously encoded objects
     */
    fun commit() = codec.toData(byteArray)
}