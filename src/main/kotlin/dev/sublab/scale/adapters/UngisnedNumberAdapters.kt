package dev.sublab.scale.adapters

import dev.sublab.scale.*
import kotlin.reflect.KType

internal fun UByte.toScaleByteArray() = byteArrayOf(toByte())

class UByteAdapter: ScaleCodecAdapter<UByte>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader.readByte().toUByte()
    override fun write(obj: UByte, type: KType) = obj.toScaleByteArray()
}

internal fun ByteArray.toUShort()
    = foldIndexed(0U) { i, result, byte -> result or (byte.toUByte().toUInt() shl 8 * i) }
        .toUShort()

internal fun UShort.toScaleByteArray() = (0..8 step 8)
    .map { toUInt() shr it }
    .map { it.toByte() }
    .toByteArray()

class UShortAdapter: ScaleCodecAdapter<UShort>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UShort.SIZE_BYTES)
        .toUShort()

    override fun write(obj: UShort, type: KType) = obj.toScaleByteArray()
}

internal fun ByteArray.toUInt()
    = foldIndexed(0U) { i, result, byte -> result or (byte.toUByte().toUInt() shl 8 * i) }

internal fun UInt.toScaleByteArray() = (0..24 step 8)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class UIntAdapter: ScaleCodecAdapter<UInt>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(UInt.SIZE_BYTES)
        .toUInt()

    override fun write(obj: UInt, type: KType) = obj.toScaleByteArray()
}

internal fun ByteArray.toULong()
    = foldIndexed(0UL) { i, result, byte -> result or (byte.toUByte().toULong() shl 8 * i) }

internal fun ULong.toScaleByteArray() = (0..56 step 8)
    .map { this shr it }
    .map { it.toByte() }
    .toByteArray()

class ULongAdapter: ScaleCodecAdapter<ULong>() {
    override fun read(reader: ByteArrayReader, type: KType) = reader
        .read(ULong.SIZE_BYTES)
        .toULong()

    override fun write(obj: ULong, type: KType) = obj.toScaleByteArray()
}