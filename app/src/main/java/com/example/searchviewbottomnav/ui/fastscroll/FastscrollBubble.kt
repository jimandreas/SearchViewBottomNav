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

@file:Suppress("MoveVariableDeclarationIntoWhen")

package com.example.searchviewbottomnav.ui.fastscroll

import android.animation.Animator
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.util.OneShotTimer
import com.example.searchviewbottomnav.util.Util
import com.example.searchviewbottomnav.util.Util.getNavigationBarSize
import timber.log.Timber

class FastscrollBubble(
        private val constraintLayout: View,
        private val recyclerView: RecyclerView,
        private val viewLifecycleOwner: LifecycleOwner,
        private val monthList : Array<String>)
    : LifecycleEventObserver, OneShotTimer.Callback, View.OnTouchListener {

    private lateinit var thumbImageView: ImageView
    private lateinit var t2015: View
    private lateinit var t2010: View
    private lateinit var t2005: View
    private lateinit var tcurrent: TextView


    private lateinit var debugTextView: TextView

    private lateinit var animatorSet: AnimatorSet

    private val thumbTimer = OneShotTimer(recyclerView.context)
    private val dateLineTimer = OneShotTimer(recyclerView.context)

    private val containerWidth = (recyclerView as ViewGroup).width

    private var adjRheight = 0


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

        /*
        playBtn.setOnTouchListener { _, event ->
            playSound(event)
            true
        }
         */
        thumbImageView.setOnTouchListener(this)

        val p = getNavigationBarSize(constraintLayout.context)
        Timber.e("NAV HEIGHT = $p")
        adjRheight = recyclerView.height - p
    }



    private fun showDateLine() {
//        t2015.visibility = VISIBLE
//        t2010.visibility = VISIBLE
//        t2005.visibility = VISIBLE

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

    private fun hideDateLine() {
        t2015.visibility = INVISIBLE
    }

    private fun slideInAnmationThumb() {

        Timber.v("slideInAnimationThumb")

        with(thumbImageView) {
            //if (visibility == View.GONE) {
                animate().cancel()
                //translationX = containerWidth.toFloat()+width
                visibility = VISIBLE
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
            //Timber.e("Scroll dy=$dy")

            val first = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val last = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            val ave = (first + last).toFloat() / 2f
            Timber.e("first = $first last = $last")
            thumbTimer.setTimerValue(3000)

            val curPos = ave * recyclerView.height.toFloat() / 252f
            thumbImageView.y = curPos
            tcurrent.y = curPos

            val str = monthList[first]
            tcurrent.text = str
        }
    }

    override fun onTouch(v: View?, m: MotionEvent?): Boolean {
        val rHeight = recyclerView.height

        if (m == null) return true
        val action = m.actionMasked
        when (action) {
            ACTION_DOWN -> {


                val pos = recyclerView.scrollY
                Timber.e("pos is $pos")
                showDateLine()
                //previousX = m.x
                previousY = m.y
                thumbTimer.setTimerValue(3000)
            }
            ACTION_MOVE -> {
                //thumbImageView.x = m.x
                thumbTimer.setTimerValue(3000)

//                thumbImageView.y = m.rawY - 2*thumbImageView.height
//                clipY()

                val pos = 252f * (m.rawY-thumbImageView.height) / (rHeight.toFloat()-thumbImageView.height)
                debugTextView.text = pos.toString()

               // recyclerView.smoothScrollToPosition(pos.toInt())
                recyclerView.scrollToPosition(pos.toInt())
                Timber.v("${m.rawY} ${m.y}  pos = $pos $rHeight")
            }
        }
        return false
    }

    private fun clipY() {
        if (thumbImageView.y < 0) {
            thumbImageView.y = 0f
        }
        if (thumbImageView.y > recyclerView.height - 2*thumbImageView.height) {
            thumbImageView.y = recyclerView.height - 2*thumbImageView.height.toFloat()
        }
    }

    companion object {
        private var previousX  = 0f
        private var previousY = 0f
    }


}