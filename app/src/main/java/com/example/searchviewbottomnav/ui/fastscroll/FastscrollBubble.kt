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

package com.example.searchviewbottomnav.ui.fastscroll

import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class FastscrollBubble(
    private val recyclerView: RecyclerView) {

    fun setup() {
        val l = Listener()
        recyclerView.addOnScrollListener(l)
    }


    private class Listener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            Timber.v("OnScrollChanged")
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            Timber.v("OnScrolled")
        }
    }

}