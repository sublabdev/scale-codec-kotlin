package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KType

internal fun Byte.toScaleByteArray() = byteArrayOf(this)

class ByteAdapter: ScaleCodecAdapter<Byte>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte()
    override fun write(obj: Byte, type: KType) = obj.toScaleByteArray()
}

internal fun Short.toScaleByteArray() = (0..8 step 8)
    .map { toInt() shr it }
    .map { it.toByte() }
    .toByteArray()

class ShortAdapter: ScaleCodecAdapter<Short>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(2)
        .foldIndexed(0) { i, result, byte -> result or (byte.toUByte().toInt() shl 8 * i) }
        .toShort()

    override fun write(obj: Short, type: KType) = obj.toScaleByteArray()
}

internal fun Int.toScaleByteArray() = (0..24 step 8)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class IntAdapter: ScaleCodecAdapter<Int>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(4)
        .foldIndexed(0) { i, result, byte -> result or (byte.toUByte().toInt() shl 8 * i) }

    override fun write(obj: Int, type: KType) = obj.toScaleByteArray()
}

internal fun Long.toScaleByteArray() = (0..56 step 8)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class LongAdapter: ScaleCodecAdapter<Long>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(8)
        .foldIndexed(0L) { i, result, byte -> result or (byte.toUByte().toLong() shl 8 * i) }

    override fun write(obj: Long, type: KType) = obj.toScaleByteArray()
}