/*
 *  Copyright 2021 James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.example.searchviewbottomnav.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

internal class MonthsKtTest {

    @Test
    @DisplayName("touch test the generated list - Dec 2020 down to Jan 2000")
    fun testGenerateMonthList() {

        val testresult = generateMonthList()

        assertFalse(testresult.isEmpty())

        assertEquals(numMonths, testresult.size)
        assert(testresult[0].contains("December"))
        assert(testresult[0].contains("2020"))
        assert(testresult[251].contains("January"))
        assert(testresult[251].contains("2000"))
    }
}