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