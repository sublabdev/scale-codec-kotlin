package dev.sublab.scale.dataContainers

import dev.sublab.scale.ScaleDataContainer

class ScaleByteArrayDataContainer: ScaleDataContainer<ByteArray> {
    override val type get() = ByteArray::class

    override fun fromByteArray(byteArray: ByteArray) = byteArray
    override fun toByteArray(data: ByteArray) = data
}