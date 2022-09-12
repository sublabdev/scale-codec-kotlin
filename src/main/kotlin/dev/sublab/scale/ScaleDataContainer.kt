package dev.sublab.scale

import kotlin.reflect.KClass

interface ScaleDataContainer<Data: Any> {
    val type: KClass<Data>

    fun fromByteArray(byteArray: ByteArray): Data
    fun toByteArray(data: Data): ByteArray
}