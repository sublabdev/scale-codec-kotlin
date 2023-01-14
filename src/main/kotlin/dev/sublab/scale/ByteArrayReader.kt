package dev.sublab.scale

/**
 * Handles ByteArray reading
 * @property byteArray an actual ByteArray to read
 */
@Suppress("unused")
class ByteArrayReader(
    private val byteArray: ByteArray
) {
    /**
     * An offset that shows how much has been read
     */
    var offset = 0
    private var lastReadSize = 0

    /**
     * Reads a ByteArray of a specified size
     * @param size The size of ByteArray to be read
     * @return A new ByteArray as result of reading the initial one
     */
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

    /**
     * Reads data by one byte (the size is set to one)
     * @return The first byte as a result of reading
     */
    fun readByte() = read(1)[0]

    /**
     * Reads data till the end
     * @return A new ByteArray as result of reading the initial one
     */
    fun readToEnd() = read(byteArray.size - offset)

    /**
     * Reverts the offset
     */
    fun revert(offset: Int) {
        this.offset -= offset
    }

    /**
     * Reverts the offset to the last read size
     */
    fun revertLast() = revert(lastReadSize)
}