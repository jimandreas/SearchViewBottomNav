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

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.databinding.FragmentFastscrollBinding
import com.example.searchviewbottomnav.util.generateMonthList

class FastscrollFragment : Fragment() {

    private var _binding: FragmentFastscrollBinding? = null
    private val binding get() = _binding!!

    private var monthList: Array<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFastscrollBinding.inflate(inflater, container, false)

        monthList = generateMonthList()
        binding.fastScrollRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.fastScrollRecyclerview.adapter = MonthListAdapter(monthList!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fsb = FastscrollBubble(binding.root, binding.fastScrollRecyclerview, viewLifecycleOwner, monthList!!)
        fsb.setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class MonthListAdapter(private val monthList: Array<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var background: Int = 0

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_MONTH
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (background == 0) {
                val typedValue = TypedValue()
                parent.context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackground,
                    typedValue,
                    true
                )
                background = typedValue.resourceId
            }

            val layoutId = if (viewType == VIEW_TYPE_HEADER) {
                R.layout.fastscroll_list_item_header
            } else {
                R.layout.fastscroll_list_item_motm
            }

            val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            view.setBackgroundResource(background)

            return if (viewType == VIEW_TYPE_HEADER) {
                HeaderViewHolder(view)
            } else {
                MonthViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is HeaderViewHolder -> holder.bind()
                is MonthViewHolder -> holder.bind(monthList[position - 1])
            }
        }

        override fun getItemCount(): Int = monthList.size + 1

        private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(R.id.columnheader)

            fun bind() {
                val htmlText = "<strong><big>FAST SCROLL</big></strong><br><i>"
                textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    @Suppress("DEPRECATION")
                    Html.fromHtml(htmlText)
                }
            }
        }

        private class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(R.id.monthtext)

            fun bind(month: String) {
                textView.text = month
            }
        }

        companion object {
            private const val VIEW_TYPE_HEADER = 0
            private const val VIEW_TYPE_MONTH = 1
        }
    }
}
