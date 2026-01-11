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

@file:Suppress("MoveVariableDeclarationIntoWhen", "LiftReturnOrAssignment")

package com.example.searchviewbottomnav.ui.fastscroll

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.MotionEvent
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
import com.example.searchviewbottomnav.ui.fastscroll.FastscrollBubble.Companion.BOTTOMSTATE.*
import com.example.searchviewbottomnav.util.OneShotTimer
import com.example.searchviewbottomnav.util.numMonths
import timber.log.Timber

class FastscrollBubble(
        private val constraintLayout: View,
        private val recyclerView: RecyclerView,
        private val viewLifecycleOwner: LifecycleOwner,
        private val monthList: Array<String>)
    : LifecycleEventObserver, OneShotTimer.Callback, View.OnTouchListener {

    private lateinit var thumbImageView: ImageView
    private lateinit var t2015: View
    private lateinit var t2010: View
    private lateinit var t2005: View
    private lateinit var tcurrent: TextView


    private lateinit var debugTextView: TextView  // set to VISIBLE for debugging

    private val thumbTimer = OneShotTimer(recyclerView.context)

    @SuppressLint("ClickableViewAccessibility")
    fun setup() {
        val l = ScrollListener()
        recyclerView.addOnScrollListener(l)
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
        with(this) {
            this.visibility = VISIBLE
            animate().apply {
                duration = 200
                alpha(1.0f)
            }
        }
    }

    private fun fadeOutDates() {
        t2015.fade()
        t2010.fade()
        t2005.fade()
        tcurrent.fade()
    }

    private fun View.fade() {
        with(this) {
            //this.visibility = VISIBLE
            animate().apply {
                duration = 200
                alpha(0.0f)
            }
        }
    }

    private fun slideInAnmationThumb() {
        with(thumbImageView) {
            animate().cancel()
            //translationX = containerWidth.toFloat()+width
            visibility = VISIBLE
            animate().apply {
                duration = 200
                setListener(null)
                translationX(-width.toFloat())
            }.start()
        }
    }

    private fun slideOutAnimationThumb() {
        val l = ThumbListener()
        with(thumbImageView) {
            animate().apply {
                duration = 500
                translationX(width.toFloat())
                setListener(l)
                start()
            }
        }
    }

    override fun timerFinished() {
        slideOutAnimationThumb()
        fadeOutDates()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

//        when (event) {
//
//            ON_CREATE -> Timber.v("On_CREATE")
//
//            ON_START -> Timber.v("On_START")
//
//            ON_RESUME -> Timber.v("On_RESUME")
//
//            ON_PAUSE -> Timber.v("On_PAUSE")
//
//            ON_STOP -> Timber.v("On_STOP")
//
//            ON_DESTROY -> Timber.v("On_DESTROY")
//
//            ON_ANY -> Timber.v("On_ANY")
//        }
    }

    inner class ThumbListener : Animator.AnimatorListener {

        override fun onAnimationEnd(animation: Animator) {
            // Timber.e("ON END")
            thumbImageView.visibility = View.GONE
        }

        override fun onAnimationStart(animation: Animator) {
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //Timber.v("OnScrollChanged newState = $newState")
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    slideInAnmationThumb()
                    //Timber.v("Dragging")
                    if (!thumbTimer.isRunning) {
                        thumbTimer.startTimer(3000)
                    } else {
                        thumbTimer.setTimerValue(3000)
                    }
                }
//                RecyclerView.SCROLL_STATE_IDLE -> Timber.v("Idle")
//                RecyclerView.SCROLL_STATE_SETTLING -> Timber.v("Settling")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val first = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val last = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
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

            thumbTimer.setTimerValue(3000)

            val curPos = ave * (recyclerView.height.toFloat() - thumbImageView.height) / numMonths.toFloat()


            val bottomDelta: Int = recyclerView.height - curPos.toInt()
            // Timber.e("first = $first last = $last curPos $curPos bDelta $bottomDelta tHeight ${thumbImageView.height}")
            // handle bottom edge condition:

            if (bottomDelta > thumbImageView.height) {
                thumbImageView.y = curPos
                tcurrent.y = curPos
            }

            val unclippedPos = ave * recyclerView.height.toFloat() / numMonths.toFloat()


            /*
             *  Bottom region handling- the thumb must stop at the boundary
             *  of the recyclerView but the scrolling should continue to the last value
             */

            var str = ""

            if (unclippedPos > recyclerView.height.toFloat() - thumbImageView.height.toFloat()) {
                if (currentState == POSITION_IS_IN_MIDDLE_SECTION) {
                    currentState = POSITION_IS_AT_BOTTOM // entering the bottom region
                    bottomAddedScrollValue = 0
                }
            } else {
                currentState = POSITION_IS_IN_MIDDLE_SECTION
            }

            when (currentState) {
                POSITION_IS_IN_MIDDLE_SECTION -> {
                    str = monthList[ave.toInt()]
                }
                POSITION_IS_AT_BOTTOM -> {
                    bottomAddedScrollValue += 1
                    if (ave.toInt() + bottomAddedScrollValue < last - 1) {
                        str = monthList[ave.toInt() + bottomAddedScrollValue]
                    } else {
                        str = monthList[last - 1]
                    }
                }
                else -> {
                }
            }

            /*
             *  Top region handling- the thumb must stop at the boundary
             *  of the recyclerView but the scrolling should continue to the last value
             */
            if (unclippedPos < thumbImageView.height.toFloat()) {
                if (currentState == POSITION_IS_IN_MIDDLE_SECTION) {
                    currentState = POSITION_IS_AT_TOP // entering the bottom region
                    topAddedScrollValue = 0
                }
            } else {
                currentState = POSITION_IS_IN_MIDDLE_SECTION
            }

            when (currentState) {
                POSITION_IS_IN_MIDDLE_SECTION -> {
                    str = monthList[ave.toInt()]
                }
                POSITION_IS_AT_TOP -> {
                    topAddedScrollValue += 1
                    if (ave.toInt() - bottomAddedScrollValue > 1) {
                        str = monthList[ave.toInt() - topAddedScrollValue]
                    } else {
                        str = monthList[0]
                    }
                }
                else -> {
                }
            }

            tcurrent.text = str
        }
    }

    override fun onTouch(v: View?, m: MotionEvent?): Boolean {
        v?.performClick()  // this does nothing but lint likes it

        val rHeight = recyclerView.height.toFloat()
        val tHeight = thumbImageView.height.toFloat()

        if (m == null) return true
        val action = m.actionMasked
        when (action) {
            ACTION_DOWN -> {
                showDateLine()
                previousY = m.y
                thumbTimer.setTimerValue(3000)
            }
            ACTION_MOVE -> {
                thumbTimer.setTimerValue(3000)
                // this is tuned - still has mild hysteresis problems.
                val pos = (m.rawY - tHeight * 1.8f) * numMonths.toFloat() / rHeight
                debugTextView.text = pos.toString()
                // recyclerView.smoothScrollToPosition(pos.toInt()) // this doesn't work
                recyclerView.scrollToPosition(pos.toInt())
                //Timber.v("${m.rawY} ${m.y}  pos $pos recyclerView Y ${recyclerView.y} Y+H ${recyclerView.y + recyclerView.height}")
            }
        }
        return false
    }

/*    private fun clipY() {
        if (thumbImageView.y < 0) {
            thumbImageView.y = 0f
        }
        if (thumbImageView.y > recyclerView.height - 2*thumbImageView.height) {
            thumbImageView.y = recyclerView.height - 2*thumbImageView.height.toFloat()
        }
    }*/

    companion object {
        private var previousY = 0f

        private var currentState = POSITION_IS_IN_MIDDLE_SECTION
        private var bottomAddedScrollValue = 0
        private var topAddedScrollValue = 0

        enum class BOTTOMSTATE {
            POSITION_IS_AT_TOP,
            POSITION_IS_IN_MIDDLE_SECTION,
            POSITION_IS_AT_BOTTOM
        }
    }


}