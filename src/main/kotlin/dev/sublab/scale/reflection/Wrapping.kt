package dev.sublab.scale.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

@Suppress("unused")
fun KClassifier.createGenericType(vararg classes: KClass<*>, nullable: Boolean = false) = createType(
    arguments = classes.map {
        KTypeProjection.invariant(it.createType())
    },
    nullable = nullable
)

@Suppress("unused")
fun KClassifier.createGenericType(vararg types: KType, nullable: Boolean = false) = createType(
    arguments = types.map {
        KTypeProjection.invariant(it)
    },
    nullable = nullable
)

/**
 * Creates a type from KClassifier with the same properties as in provided KType
 * If creation fails (IllegalArgumentException thrown), returns null
 * Used when comparing generic types for matching in adapter provider
 */
@Suppress("unused")
fun KClassifier.createFromType(type: KType) = try {
    createType(
        arguments = type.arguments,
        nullable = type.isMarkedNullable,
        annotations = type.annotations
    )
} catch (e: Exception) {
    null
}