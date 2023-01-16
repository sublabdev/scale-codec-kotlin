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

import dev.sublab.common.numerics.UInt32
import dev.sublab.scale.adapters.UInt32Adapter
import dev.sublab.scale.support.BaseTest
import dev.sublab.scale.support.TEST_REPEATS
import kotlin.test.Test
import kotlin.test.assertEquals

class TestUInt32: BaseTest<UInt32>() {
    override val type = UInt32::class
    override val adapter = UInt32Adapter()

    override val testValues
        get() = (0 until TEST_REPEATS).map { (UInt32.MIN_VALUE..UInt32.MAX_VALUE).random() }

    @Test
    internal fun testOneUIntCoding() {
        val testValue = 134578345u
        val scaleCodec = ScaleCodec.default()

        val encodedValue = scaleCodec.toScale(testValue, UInt32::class)
        val decoded = scaleCodec.fromScale(encodedValue, UInt32::class)

        if (testValue != decoded) {
            println("Expected: $testValue, decoded: $decoded")
        }
        assertEquals(testValue, decoded)
    }
}