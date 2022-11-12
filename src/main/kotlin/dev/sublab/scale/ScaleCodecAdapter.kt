package dev.sublab.scale

import dev.sublab.scale.adapters.*
import dev.sublab.scale.annotations.EnumClass
import dev.sublab.scale.reflection.createFromType
import dev.sublab.scale.reflection.nullableCreateType
import java.lang.Exception
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation

class NoAdapterKnown(val type: KType? = null): Throwable()
class NoNullableAdapterException: Throwable()

open class ScaleCodecReadException(reason: String?): Throwable(reason)
open class ScaleCodecWriteException(reason: String?): Throwable(reason)

abstract class ScaleCodecAdapter<T> {
    @Throws(ScaleCodecReadException::class)
    fun read(byteArray: ByteArray, type: KType): T = read(ByteArrayReader(byteArray), type)

    // Implement in adapters

    @Throws(ScaleCodecReadException::class)
    abstract fun read(reader: ByteArrayReader, type: KType): T

    @Throws(ScaleCodecWriteException::class)
    abstract fun write(obj: T, type: KType): ByteArray
}

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(byteArray: ByteArray, type: KClass<T>) = read(byteArray, type.createType())

@Throws(ScaleCodecReadException::class)
fun <T: Any> ScaleCodecAdapter<T>.read(reader: ByteArrayReader, type: KClass<T>) = read(reader, type.createType())

@Throws(ScaleCodecWriteException::class)
fun <T: Any> ScaleCodecAdapter<T>.write(obj: T, type: KClass<T>) = write(obj, type.createType())