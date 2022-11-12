package dev.sublab.scale.reflection

import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

internal fun listOfBytesType() = List::class.createType(
    arguments = listOf(
        KTypeProjection.invariant(Byte::class.createType())
    )
)