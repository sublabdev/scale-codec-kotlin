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

import kotlin.reflect.*
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

fun KClassifier.nullableCreateType() = try {
    createType()
} catch (e: Exception) {
    null
}

//fun KParameter.annotatedType() = try {
//    type.classifier?.createType(
//        arguments = type.arguments,
//        nullable = type.isMarkedNullable,
//        annotations = type.annotations.union(annotations).toList()
//    )
//} catch (e: Exception) {
//    null
//}

//fun <V> KProperty<V>.annotatedReturnType() = try {
//    returnType.classifier?.createType(
//        arguments = returnType.arguments,
//        nullable = returnType.isMarkedNullable,
//        annotations = returnType.annotations.union(annotations).toList()
//    )
//} catch (e: Exception) {
//    null
//}