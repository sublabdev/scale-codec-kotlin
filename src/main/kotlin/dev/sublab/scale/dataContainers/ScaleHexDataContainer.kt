package dev.sublab.scale.dataContainers

import dev.sublab.hex.hex
import dev.sublab.scale.ScaleDataContainer

class ScaleHexDataContainer: ScaleDataContainer<String> {
    override val type get() = String::class

    /**
     * Converts ByteArray to hex encoded String
     */
    override fun fromByteArray(byteArray: ByteArray) = byteArray.hex.encode()

    /**
     * Converts String to hex decoded ByteArray
     */
    override fun toByteArray(data: String) = data.hex.decode()
}