package dev.sublab.scale.reflection

import kotlin.reflect.KType

open class UnwrappingException: Throwable()

@Suppress("unused")
class NonParameterizedTypeException: UnwrappingException()

@Suppress("unused")
class UnexpectedParameterizedTypeException(val expected: KType, val revealed: KType): UnwrappingException()

@Suppress("unused")
class UnexpectedCountOfArgumentsInParameterizedTypeException(val expectedCount: Int, val count: Int): UnwrappingException()

@Suppress("unused")
fun KType.unwrapArguments() = arguments.mapNotNull { it.type }

@Suppress("unused")
fun KType.unwrapArgument(): KType {
    val arguments = unwrapArguments()
    if (arguments.count() != 1) {
        throw UnexpectedCountOfArgumentsInParameterizedTypeException(1, arguments.count())
    }

    return arguments[0]
}