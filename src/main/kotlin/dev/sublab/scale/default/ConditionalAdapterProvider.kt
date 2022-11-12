package dev.sublab.scale.default

import kotlin.reflect.KType

internal typealias ConditionalAdapterProvider = (KType) -> AdapterProvider?
