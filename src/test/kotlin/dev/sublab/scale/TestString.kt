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

package dev.sublab.scale

import dev.sublab.scale.adapters.StringAdapter
import dev.sublab.scale.default.DefaultScaleCodecAdapterProvider
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.util.*

internal class TestString: BaseTest<String>() {
    override val type = String::class
    override val adapter = StringAdapter(DefaultScaleCodecAdapterProvider())
    override val testValues = (1..TEST_REPEATS).map {
        UUID.randomUUID().toString()
    }
}