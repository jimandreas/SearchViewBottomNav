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

package com.example.searchviewbottomnav.ui.fastscroll

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.MotionEvent
import java.util.Locale
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.util.OneShotTimer
import com.example.searchviewbottomnav.util.numMonths
import timber.log.Timber

class FastscrollBubble(
    private val constraintLayout: View,
    private val recyclerView: RecyclerView,
    private val viewLifecycleOwner: LifecycleOwner,
    private val monthList: Array<String>
) : LifecycleEventObserver, OneShotTimer.Callback, View.OnTouchListener {

    private lateinit var thumbImageView: ImageView
    private lateinit var t2015: View
    private lateinit var t2010: View
    private lateinit var t2005: View
    private lateinit var tcurrent: TextView
    private lateinit var debugTextView: TextView

    private val thumbTimer = OneShotTimer()

    // Instance state (not companion object to avoid issues with multiple instances)
    private var previousY = 0f
    private var currentState = ScrollState.MIDDLE
    private var bottomAddedScrollValue = 0
    private var topAddedScrollValue = 0

    @SuppressLint("ClickableViewAccessibility")
    fun setup() {
        recyclerView.addOnScrollListener(ScrollListener())
        viewLifecycleOwner.lifecycle.addObserver(this)

        thumbImageView = constraintLayout.findViewById(R.id.thumbFastscrollerImageview)
        t2015 = constraintLayout.findViewById(R.id.t2015)
        t2010 = constraintLayout.findViewById(R.id.t2010)
        t2005 = constraintLayout.findViewById(R.id.t2005)
        tcurrent = constraintLayout.findViewById(R.id.tcurrent)
        debugTextView = constraintLayout.findViewById(R.id.debugTextView)

        thumbTimer.setCallback(this)
        thumbImageView.setOnTouchListener(this)
    }

    private fun showDateLine() {
        t2015.show()
        t2010.show()
        t2005.show()
        tcurrent.show()
    }

    private fun View.show() {
        visibility = VISIBLE
        animate().apply {
            duration = ANIMATION_DURATION_SHORT
            alpha(1.0f)
        }
    }

    private fun fadeOutDates() {
        t2015.fade()
        t2010.fade()
        t2005.fade()
        tcurrent.fade()
    }

    private fun View.fade() {
        animate().apply {
            duration = ANIMATION_DURATION_SHORT
            alpha(0.0f)
        }
    }

    private fun slideInAnimationThumb() {
        with(thumbImageView) {
            animate().cancel()
            visibility = VISIBLE
            animate().apply {
                duration = ANIMATION_DURATION_SHORT
                setListener(null)
                translationX(-width.toFloat())
            }.start()
        }
    }

    private fun slideOutAnimationThumb() {
        with(thumbImageView) {
            animate().apply {
                duration = ANIMATION_DURATION_LONG
                translationX(width.toFloat())
                setListener(ThumbListener())
                start()
            }
        }
    }

    override fun timerFinished() {
        slideOutAnimationThumb()
        fadeOutDates()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            thumbTimer.cancelTimer()
        }
    }

    private inner class ThumbListener : Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator) {
            thumbImageView.visibility = View.GONE
        }

        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }

    private inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                slideInAnimationThumb()
                if (!thumbTimer.isRunning) {
                    thumbTimer.startTimer(TIMER_DURATION)
                } else {
                    thumbTimer.setTimerValue(TIMER_DURATION)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val first = layoutManager.findFirstCompletelyVisibleItemPosition()
            val last = layoutManager.findLastCompletelyVisibleItemPosition()
            if (first < 0 || last < 0) {
                return
            }

            var ave = (first + last).toFloat() / 2f
            if (ave < 0f) {
                Timber.e("ave is $ave, first $first last $last")
            }
            if (ave >= monthList.size) {
                ave = (monthList.size - 1).toFloat()
            }

            thumbTimer.setTimerValue(TIMER_DURATION)

            val curPos = ave * (recyclerView.height.toFloat() - thumbImageView.height) / numMonths.toFloat()
            val bottomDelta: Int = recyclerView.height - curPos.toInt()

            if (bottomDelta > thumbImageView.height) {
                thumbImageView.y = curPos
                tcurrent.y = curPos
            }

            val unclippedPos = ave * recyclerView.height.toFloat() / numMonths.toFloat()

            // Bottom region handling - thumb stops at boundary but scrolling continues
            var str = ""

            if (unclippedPos > recyclerView.height.toFloat() - thumbImageView.height.toFloat()) {
                if (currentState == ScrollState.MIDDLE) {
                    currentState = ScrollState.BOTTOM
                    bottomAddedScrollValue = 0
                }
            } else {
                currentState = ScrollState.MIDDLE
            }

            when (currentState) {
                ScrollState.MIDDLE -> {
                    str = monthList[ave.toInt()]
                }
                ScrollState.BOTTOM -> {
                    bottomAddedScrollValue += 1
                    str = if (ave.toInt() + bottomAddedScrollValue < last - 1) {
                        monthList[ave.toInt() + bottomAddedScrollValue]
                    } else {
                        monthList[last - 1]
                    }
                }
                ScrollState.TOP -> {}
            }

            // Top region handling - thumb stops at boundary but scrolling continues
            if (unclippedPos < thumbImageView.height.toFloat()) {
                if (currentState == ScrollState.MIDDLE) {
                    currentState = ScrollState.TOP
                    topAddedScrollValue = 0
                }
            } else {
                currentState = ScrollState.MIDDLE
            }

            when (currentState) {
                ScrollState.MIDDLE -> {
                    str = monthList[ave.toInt()]
                }
                ScrollState.TOP -> {
                    topAddedScrollValue += 1
                    str = if (ave.toInt() - bottomAddedScrollValue > 1) {
                        monthList[ave.toInt() - topAddedScrollValue]
                    } else {
                        monthList[0]
                    }
                }
                ScrollState.BOTTOM -> {}
            }

            tcurrent.text = str
        }
    }

    override fun onTouch(v: View?, m: MotionEvent?): Boolean {
        v?.performClick()

        if (m == null) return true
        val action = m.actionMasked
        when (action) {
            ACTION_DOWN -> {
                showDateLine()
                previousY = m.y
                thumbTimer.setTimerValue(TIMER_DURATION)
            }
            ACTION_MOVE -> {
                thumbTimer.setTimerValue(TIMER_DURATION)
                val rHeight = recyclerView.height.toFloat()
                val tHeight = thumbImageView.height.toFloat()
                val pos = (m.rawY - tHeight * TOUCH_OFFSET_MULTIPLIER) * numMonths.toFloat() / rHeight
                debugTextView.text = String.format(Locale.ROOT, "%.1f", pos)
                recyclerView.scrollToPosition(pos.toInt())
            }
        }
        return false
    }

    private enum class ScrollState {
        TOP,
        MIDDLE,
        BOTTOM
    }

    companion object {
        private const val ANIMATION_DURATION_SHORT = 200L
        private const val ANIMATION_DURATION_LONG = 500L
        private const val TIMER_DURATION = 3000
        private const val TOUCH_OFFSET_MULTIPLIER = 1.8f
    }
}
