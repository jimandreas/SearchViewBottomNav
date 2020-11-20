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

@file:Suppress("UNUSED_PARAMETER", "CanBeParameter")

package com.example.searchviewbottomnav.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewbottomnav.Fruits
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.ui.fruit.FruitActivity
import com.example.searchviewbottomnav.util.PrefsUtil

class SearchMatchesFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchResultsDisplay: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterInUse: SimpleStringRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_matches, container, false)

        searchResultsDisplay = root.findViewById(R.id.search_results_matches_frame_layout)
        recyclerView = root.findViewById(R.id.search_results_list)

        adapterInUse = SimpleStringRecyclerViewAdapter(root.context, listOf(""))
        adapterInUse.setViewModel(searchViewModel)
        with(recyclerView) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = adapterInUse
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (callback != null && dy > 0) {
                    callback!!.clearKeyboard()
                }
            }
        })
        return root
    }

    fun setViewModel(viewModel: SearchViewModel) {
        searchViewModel = viewModel
    }

    fun show() {
        searchResultsDisplay.visibility = VISIBLE
    }

    fun hide() {
        searchResultsDisplay.visibility = GONE
    }

    fun isShowing(): Boolean {
        return searchResultsDisplay.visibility == VISIBLE
    }

    fun startSearch(searchTerm: String) {
        val matches = Fruits.searchFruit(searchTerm)
        val matchNames = matches.map { it.name }
        adapterInUse.updateStringList(matchNames)
        adapterInUse.notifyDataSetChanged()
    }

    class SimpleStringRecyclerViewAdapter(context: Context, private val valuesIn: List<String>)
        : RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>() {

        private var stringListToDisplay = valuesIn
        private lateinit var searchViewModel: SearchViewModel

        class ViewHolder(theView: View) : RecyclerView.ViewHolder(theView) {
            lateinit var boundString: String
            val textView: TextView = theView.findViewById(R.id.fragment_search_text)
                    as TextView
        }

        fun setViewModel(model: SearchViewModel) {
            searchViewModel = model
        }

        fun updateStringList(newList: List<String>) {
            stringListToDisplay = newList
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_search_item_textview, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.boundString = stringListToDisplay[position]
            holder.textView.text = stringListToDisplay[position]

            /*
             * On a click of a match, start the FruitActivity
             * and pass it the fruit name.
             */
            holder.textView.setOnClickListener { v ->
                val context = v.context
                val intent = Intent(context, FruitActivity::class.java)
                val fruitName = holder.boundString
                intent.putExtra(FruitActivity.EXTRA_NAME, fruitName)

                searchViewModel.updateSearchStringList(fruitName)

                val oldSet = PrefsUtil.getStringSet(
                        PrefsUtil.PREVIOUS_SEARCHES_KEY,
                        emptySet())
                val newSet: MutableSet<String>
                if (oldSet == null) {
                    newSet = mutableSetOf(fruitName)
                } else {
                    newSet = oldSet.toMutableSet()
                    newSet.add(fruitName)
                }
                PrefsUtil.setStringSet(
                        PrefsUtil.PREVIOUS_SEARCHES_KEY,
                        newSet)

                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return stringListToDisplay.size
        }
    }

    fun setCallback(callbackIn: Callback) {
        callback = callbackIn
    }

    interface Callback {
        fun clearKeyboard()
    }

    private var callback: Callback? = null
}