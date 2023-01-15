/**
 *
 * Copyright 2023 SUBSTRATE LABORATORY LLC <info@sublab.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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