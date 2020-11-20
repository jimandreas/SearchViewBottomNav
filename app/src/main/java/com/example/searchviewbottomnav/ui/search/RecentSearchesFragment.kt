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

@file:Suppress("UNUSED_ANONYMOUS_PARAMETER", "LiftReturnOrAssignment", "RedundantSamConstructor", "ImplicitThis", "ImplicitThis", "RedundantLambdaArrow")

package com.example.searchviewbottomnav.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.ui.fruit.FruitActivity
import com.example.searchviewbottomnav.util.PrefsUtil

class RecentSearchesFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var recentSearchesContainer: FrameLayout
    private lateinit var recentSearchesList: ListView
    private lateinit var recentSearchesDeleteButton: ImageView
    private lateinit var wiredList: MutableList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_recent, container, false)
        //localContext = root!!.context

        recentSearchesContainer = root.findViewById(R.id.recent_searches_frame_layout)
        recentSearchesList = root.findViewById(R.id.recent_searches_list)
        recentSearchesDeleteButton = root.findViewById(R.id.recent_searches_delete_button)

        val previousSet = PrefsUtil.getStringSet(
                PrefsUtil.PREVIOUS_SEARCHES_KEY,
                emptySet())
        if (previousSet != null && previousSet.isNotEmpty()) {
            wiredList = previousSet.toMutableList()
        } else {
            wiredList = arrayListOf()
            wiredList.clear()
        }

        val newAdapter = ArrayAdapter(
                requireContext(), R.layout.fragment_search_item_textview, wiredList)

        recentSearchesList.adapter = newAdapter
        searchViewModel.previousSearchStringList.observe(viewLifecycleOwner, {
            val searchList = it
            wiredList = searchList!!.toMutableList()
        })

        recentSearchesDeleteButton.setOnClickListener { _ ->
            wiredList.clear()
            newAdapter.clear()
            PrefsUtil.setStringSet(PrefsUtil.PREVIOUS_SEARCHES_KEY, emptySet())
            newAdapter.notifyDataSetChanged()
        }

        recentSearchesList.setOnItemClickListener { parent, v, position, id ->
            val intent = Intent(context, FruitActivity::class.java)
            val fruitName = (v as TextView).text
            intent.putExtra(FruitActivity.EXTRA_NAME, fruitName)
            requireContext().startActivity(intent)
        }
        return root
    }


    fun setViewModel(viewModel: SearchViewModel) {
        searchViewModel = viewModel
    }

    fun show() {
        recentSearchesContainer.visibility = VISIBLE
    }

    fun hide() {
        recentSearchesContainer.visibility = INVISIBLE
    }


}