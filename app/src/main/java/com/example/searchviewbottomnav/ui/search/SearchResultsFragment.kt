package com.example.searchviewbottomnav.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.searchviewbottomnav.R

class SearchResultsFragment : Fragment() {

    private lateinit var searchResultsDisplay : FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_results, container, false)

        searchResultsDisplay = root.findViewById(R.id.search_results_display)
        return root
    }

    fun show() {
        searchResultsDisplay.setVisibility(View.VISIBLE)
    }

    fun hide() {
        searchResultsDisplay.setVisibility(View.GONE)
    }
}