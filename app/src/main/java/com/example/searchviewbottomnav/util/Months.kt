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

@file:Suppress("UnnecessaryVariable")

package com.example.searchviewbottomnav.util

private val monthNamesShort = arrayOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
)


fun monthStringByKey(key: Int): String {
    if (key < 1) return ("INVALID DATE")
    val month = (key - 1) % 12
    val year = (key - 1) / 12 + 2000

    val retString = monthNamesShort[month] + " " + String.format("%4d", year)
    // String.format("%6.2f", elapsed_time);

    return retString
}

fun generateMonthList(): Array<String> {
    val initList = mutableListOf<String>()

    var j = 0
    for (i in numMonths downTo 1) {
        initList.add(monthStringByKey(i))
    }
    return initList.toTypedArray()
}

const val numMonths = 253