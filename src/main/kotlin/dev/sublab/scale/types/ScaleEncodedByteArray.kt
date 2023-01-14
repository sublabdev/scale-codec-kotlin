package dev.sublab.scale.types

/**
 * A scake encoded byte array wrapper
 */
data class ScaleEncodedByteArray(val value: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScaleEncodedByteArray

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }
}

/**
 * ByteArray to scale-encoded converter
 */
@Suppress("unused")
fun ByteArray.asScaleEncoded() = ScaleEncodedByteArray(this)