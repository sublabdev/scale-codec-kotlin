package dev.sublab.scale.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

data class TypeHolder(
    val type: KType? = null,
    val clazz: KClass<*>? = null
) {
    fun conformsTo(type: KType) = this.type?.equals(type)
        ?: clazz?.createFromType(type)?.equals(type)
        ?: false

    fun conformsTo(type: KClass<*>) = clazz?.equals(type)
        ?: this.type?.equals(type.createType())
        ?: false
}