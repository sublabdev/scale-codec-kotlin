package dev.sublab.scale

import kotlin.reflect.KClass

/**
 * An interface for converting a generic Data to ByteArray and vica versa
 */
interface ScaleDataContainer<Data: Any> {
    val type: KClass<Data>

    /**
     * Converts ByteArray to a generic Data
     */
    fun fromByteArray(byteArray: ByteArray): Data

    /**
     * Converts a generic Data to ByteArray
     */
    fun toByteArray(data: Data): ByteArray
}