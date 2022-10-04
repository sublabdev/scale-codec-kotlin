package dev.sublab.scale

@Suppress("unused")
class ByteArrayReader(
    private val byteArray: ByteArray
) {
    var offset = 0
    private var lastReadSize = 0

    @Throws(IndexOutOfBoundsException::class)
    fun read(size: Int): ByteArray {
        val endIndex = offset + size

        try {
            val result = byteArray.copyOfRange(offset, endIndex)
            offset += size
            return result
        } catch (e: Exception) {
            // Re-throw for debugging purposes
            throw e
        }
    }

    fun readByte() = read(1)[0]
    fun readToEnd() = read(byteArray.size - offset)

    fun revert(offset: Int) {
        this.offset -= offset
    }

    fun revertLast() = revert(lastReadSize)
}