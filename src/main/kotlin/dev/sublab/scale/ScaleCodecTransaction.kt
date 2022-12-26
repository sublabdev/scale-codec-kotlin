package dev.sublab.scale

import kotlin.reflect.KClass
import kotlin.reflect.KType

class ScaleCodecTransaction<Data: Any>(private val codec: ScaleCodec<Data>) {
    private var byteArray = byteArrayOf()

    @Throws
    fun <T> append(obj: T, type: KType) = apply {
        byteArray += codec.fromData(codec.toScale(obj, type))
    }

    @Throws
    fun <T: Any> append(obj: T, type: KClass<T>) = apply {
        byteArray += codec.fromData(codec.toScale(obj, type))
    }

    fun commit() = codec.toData(byteArray)
}