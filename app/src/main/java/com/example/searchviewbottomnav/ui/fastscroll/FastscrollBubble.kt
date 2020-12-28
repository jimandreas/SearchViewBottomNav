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

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.util.OneShotTimer
import timber.log.Timber

class FastscrollBubble(
        private val constraintLayout: View,
        private val recyclerView: RecyclerView,
        private val viewLifecycleOwner: LifecycleOwner)
    : LifecycleEventObserver, OneShotTimer.Callback, View.OnTouchListener {

    private lateinit var thumbImageView: ImageView
    private lateinit var t2015: View
    private var thumbImageViewX = 0
    private var t2015X = 0


    private lateinit var animatorSet: AnimatorSet

    private val thumbTimer = OneShotTimer(recyclerView.context)
    private val dateLineTimer = OneShotTimer(recyclerView.context)

    private val containerWidth = (recyclerView as ViewGroup).width


    @SuppressLint("ClickableViewAccessibility")
    fun setup() {
        val l = Listener()
        recyclerView.addOnScrollListener(l)
        viewLifecycleOwner.lifecycle.addObserver(this)

        thumbImageView = constraintLayout.findViewById(R.id.thumbFastscrollerImageview)
        t2015 = constraintLayout.findViewById(R.id.t2015)

        thumbTimer.setCallback(this)

        /*
        playBtn.setOnTouchListener { _, event ->
            playSound(event)
            true
        }
         */
        thumbImageView.setOnTouchListener(this)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Timber.e("CLICKYYYY")
        thumbTimer.setTimerValue(3000)
        showDateLine()
        return true
    }

    private fun showDateLine() {
        t2015.visibility = VISIBLE

    }

    private fun hideDateLine() {
        t2015.visibility = INVISIBLE
    }

    private fun slideInAnmationThumb() {

        Timber.v("slideInAnimationThumb")

        with(thumbImageView) {
            //if (visibility == View.GONE) {
                animate().cancel()
                //translationX = containerWidth.toFloat()+width
                visibility = View.VISIBLE
                animate().apply {
                    duration = 200
                    setListener(null)
                    translationX(-width.toFloat())
                }.start()
            //}
        }
    }

    private fun slideOutAnimationThumb() {

        Timber.v("slideOUTAnimationThumb")
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

    private fun slideOutAnimationDates() {
        // Create an animator that moves the view from a starting position right about the container
        // to an ending position right below the container. Set an accelerate interpolator to give
        // it a gravity/falling feel
        val mover = ObjectAnimator.ofFloat(t2015, View.TRANSLATION_X, 0f, 500f)
        mover.interpolator = AccelerateInterpolator(1f)
        // Use an AnimatorSet to play the falling and rotating animators in parallel for a duration
        // of a half-second to two seconds
        animatorSet = AnimatorSet()
        with(animatorSet) {
            playTogether(mover)
            duration = 500.toLong()
        }
    }


    private fun slideInAnimationDates() {

    }

    override fun timerFinished() {
        slideOutAnimationThumb()
        slideOutAnimationDates()
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

        override fun onAnimationEnd(animation: Animator?) {
            Timber.e("ON END")
            thumbImageView.visibility = View.GONE
        }

        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationRepeat(animation: Animator?) {

        }

    }

    inner class Listener : RecyclerView.OnScrollListener() {
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
            thumbTimer.setTimerValue(3000)
        }
    }


}