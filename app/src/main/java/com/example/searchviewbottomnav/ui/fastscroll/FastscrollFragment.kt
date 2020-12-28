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

@file:Suppress("RedundantSamConstructor", "UnnecessaryVariable")

package com.example.searchviewbottomnav.ui.fastscroll

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.util.generateMonthList
import com.example.searchviewbottomnav.util.monthStringByKey

class FastscrollFragment : Fragment() {

    private lateinit var fastscrollViewModel: FastscrollViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var rootView : View


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fastscrollViewModel =
                ViewModelProvider(this).get(FastscrollViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        recyclerView = root.findViewById(R.id.fast_scroll_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        val adapter = SimpleStringRecyclerViewAdapter(recyclerView.context,
            generateMonthList()
        )
        recyclerView.adapter = adapter




        rootView = root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foo = FastscrollBubble(rootView, recyclerView, viewLifecycleOwner)
        foo.setup()

    }



    class SimpleStringRecyclerViewAdapter(context: Context, private val monthList: Array<String>)
        : RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>() {

        private val typedValue = TypedValue()
        private val background: Int

        class ViewHolder(val v: View, viewType: Int) : RecyclerView.ViewHolder(v) {
            var boundString: String? = null
            lateinit var imageView: ImageView
            lateinit var textView: TextView
            lateinit var textView2: TextView
            lateinit var textViewHeader: TextView


            init {
                when (viewType) {
                    VIEW_TYPE_MOTM -> {
                        textView = v.findViewById(R.id.monthtext)
                    }
                    VIEW_TYPE_HEADER -> {
                        textViewHeader = v.findViewById(R.id.columnheader)
                    }
                }
            }

            override fun toString(): String {
                return super.toString() + " '" + textView.text
            }
        }

        // Add a header for position 0
        override fun getItemViewType(position: Int): Int {
            val entry = monthList[position]

            return if (position == 0)
                VIEW_TYPE_HEADER
            else
                VIEW_TYPE_MOTM
        }

        fun getValueAt(position: Int): String {
            return monthList[position]
        }

        init {
            context.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
            background = typedValue.resourceId
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            var layoutId = R.layout.list_item_motm
            if (viewType == VIEW_TYPE_HEADER) {
                layoutId = R.layout.list_item_header
            }

            val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            view.setBackgroundResource(background)
            return ViewHolder(view, viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.boundString = monthStringByKey(position)

            if (position == 0) {
                val spannedString = Html.fromHtml(
                    "<strong><big>"
                            + "FAST SCROLL"
                            + "</big></strong><br><i>"
                )

                holder.textViewHeader.text = spannedString
            } else {
                holder.textView.text = holder.boundString!!
            }

        }

        override fun getItemCount(): Int {
            return monthList.size
        }
    }
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_MOTM = 1
    }
}