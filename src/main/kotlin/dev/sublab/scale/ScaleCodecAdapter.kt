package dev.sublab.scale

import dev.sublab.scale.adapters.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class NoAdapterKnown(val type: KType? = null): Throwable()
class NoNullableAdapterException: Throwable()

open class ScaleCodecReadException(reason: String?): Throwable(reason)
open class ScaleCodecWriteException(reason: String?): Throwable(reason)

abstract class ScaleCodecAdapter<T> {
    @Throws(ScaleCodecReadException::class)
    fun read(byteArray: ByteArray, type: KType, annotations: List<Annotation> = listOf()): T = read(
        ByteArrayReader(byteArray),
        type
    )

    // Implement in adapters

    @Throws(ScaleCodecReadException::class)
    abstract fun read(reader: ByteArrayReader, type: KType, annotations: List<Annotation> = listOf()): T

    @Throws(ScaleCodecWriteException::class)
    abstract fun write(obj: T, type: KType, annotations: List<Annotation> = listOf()): ByteArray

}

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(byteArray: ByteArray, type: KClass<T>) = read(byteArray, type.createType())

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(reader: ByteArrayReader, type: KClass<T>) = read(reader, type.createType())

@Throws(ScaleCodecWriteException::class)
fun <T: Any> ScaleCodecAdapter<T>.write(obj: T, type: KClass<T>) = write(obj, type.createType())