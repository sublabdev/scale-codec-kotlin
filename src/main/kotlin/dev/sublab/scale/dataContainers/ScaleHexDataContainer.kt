package dev.sublab.scale.dataContainers

import dev.sublab.hex.hex
import dev.sublab.scale.ScaleDataContainer

class ScaleHexDataContainer: ScaleDataContainer<String> {
    override val type get() = String::class

    override fun fromByteArray(byteArray: ByteArray) = byteArray.hex.encode()
    override fun toByteArray(data: String) = data.hex.decode()
}