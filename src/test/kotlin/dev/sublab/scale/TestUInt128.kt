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

import dev.sublab.common.numerics.UInt128
import dev.sublab.scale.adapters.UInt128Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import java.math.BigInteger
import java.util.*

class TestUInt128: BaseTest<UInt128>() {
    override val type = UInt128::class
    override val adapter = UInt128Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS)
            .map { BigInteger(128, Random()) }
            .map { UInt128(it) }
}