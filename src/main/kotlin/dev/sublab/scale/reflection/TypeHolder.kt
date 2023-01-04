package dev.sublab.scale.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.isSupertypeOf

data class TypeHolder(
    val type: KType? = null,
    val clazz: KClass<*>? = null
) {
    private fun typesConform(lhs: KType, rhs: KType) = lhs == rhs || lhs.isSupertypeOf(rhs)
    private fun classesConform(lhs: KClass<*>, rhs: KClass<*>) = lhs == rhs || lhs.isSuperclassOf(rhs)

    fun conformsTo(type: KType) = this.type?.let {
        typesConform(it, type)
    } ?: clazz?.let {
        (type.classifier as? KClass<*>)?.let { typeClass ->
            classesConform(it, typeClass)
        }
    } ?: false

    fun conformsTo(type: KClass<*>) = clazz?.let {
        classesConform(it, type)
    } ?: this.type?.let {
        (it.classifier as? KClass<*>)?.let { typeClass ->
            classesConform(typeClass, type)
        }
    } ?: false
}