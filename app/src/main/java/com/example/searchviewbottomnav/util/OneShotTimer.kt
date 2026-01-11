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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

class OneShotTimer {

    private val timerJob = SupervisorJob()
    private val timerScope = CoroutineScope(Dispatchers.IO + timerJob)
    private val mutex = Mutex()
    private var countDownTime = 0
    var isRunning = false
        private set
    private var wasCancelled = false
    private var callback: Callback? = null

    fun setTimerValue(t: Int) = runBlocking {
        timerScope.launch {
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
        timerScope.launch {
            while (countDownTime > 0) {
                delay(TICK_INTERVAL)
                mutex.withLock {
                    countDownTime -= TICK_INTERVAL.toInt()
                }
            }

            if (!wasCancelled) {
                try {
                    withContext(Dispatchers.Main) {
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
        timerJob.cancel()
        isRunning = false
    }

    fun setCallback(callbackIn: Callback) {
        callback = callbackIn
    }

    interface Callback {
        fun timerFinished()
    }

    companion object {
        private const val TICK_INTERVAL = 500L
    }
}
