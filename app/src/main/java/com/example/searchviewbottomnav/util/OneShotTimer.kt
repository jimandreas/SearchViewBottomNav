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

@file:Suppress("unused")

package com.example.searchviewbottomnav.util

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber


class OneShotTimer(context: Context) {

    private val oneShotTimerJob = Job()
    private val mutex = Mutex()
    private var countDownTime = 0 // milliseconds
    var isRunning = false
    private var wasCancelled = false

    fun setTimerValue(t: Int) = runBlocking {
        GlobalScope.launch(Dispatchers.IO + oneShotTimerJob) {
            if (countDownTime < 0) {
                Timber.e("No NEGATIVE times please")
            } else {
                mutex.withLock {
                    countDownTime = t
                }
            }
        }
    }

    fun startTimer(t: Int) = runBlocking {
        mutex.withLock {
            countDownTime = t
            isRunning = true
        }
        wasCancelled = false
        GlobalScope.launch(Dispatchers.IO + oneShotTimerJob) {
            while (countDownTime > 0) {
                delay(500L)
                mutex.withLock {
                    countDownTime -= 500
                    //Timber.v("TIME IS $countDownTime")
                }
            }

            if (!wasCancelled) {
                try {
                    // Timber.e("CALLING CALLBACK")
                    MainScope().launch {
                        callback?.timerFinished()
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error on OneShotTimer callback")
                }
                mutex.withLock {
                    isRunning = false
                }
            }
        }
    }

    fun cancelTimer() {
        wasCancelled = true
        oneShotTimerJob.cancel()
        isRunning = false
    }

    fun setCallback(callbackIn: Callback) {
        callback = callbackIn
    }

    interface Callback {
        fun timerFinished()
    }

    private var callback: Callback? = null
}