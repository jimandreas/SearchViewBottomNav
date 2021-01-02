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

package com.example.searchviewbottomnav

import com.example.searchviewbottomnav.Fruits.searchFruit
import com.example.searchviewbottomnav.Fruits.getUrl
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

internal class FruitsTest {

    @Test
    @DisplayName("test substring search of fruit list")
    fun searchFruitTest() {
        val aterMelon = searchFruit("AterMelON")
        assertEquals(1, aterMelon.size)
        assertEquals("Watermelon", aterMelon[0].name)
    }

    @Test
    fun getURLTest() {
        val watermelon = getUrl("Watermelon")
        assertEquals("https://i.imgur.com/F9HNpLK.jpg", watermelon)
    }
}