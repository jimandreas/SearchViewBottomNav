/*
 *  Copyright 2020 James Andreas
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

private val monthNames = arrayOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

fun monthStringByKey(key: Int): String {
    if (key < 1) return ("INVALID DATE")
    val month = (key - 1) % 12
    val year = (key - 1) / 12 + 2000

    val retString = monthNames[month] + " " + String.format("%4d", year)
    // String.format("%6.2f", elapsed_time);

    return retString
}

fun generateMonthList(): Array<String> {
    val initList = mutableListOf<String>()

    var j = 0
    for (i in 252 downTo 1) {
        initList.add(monthStringByKey(i))
    }
    return initList.toTypedArray()
}