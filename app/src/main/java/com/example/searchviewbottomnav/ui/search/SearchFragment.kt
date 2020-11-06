@file:Suppress("RedundantSamConstructor", "UNUSED_ANONYMOUS_PARAMETER")

package com.example.searchviewbottomnav.ui.search

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.util.Util.hideSoftKeyboard
import timber.log.Timber

class SearchFragment :
    Fragment(),
    SearchMatchesFragment.Callback,
    TextView.OnEditorActionListener{

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var recentSearchesFragment: RecentSearchesFragment
    private lateinit var searchMatchesFragment: SearchMatchesFragment
    private lateinit var searchEditText: EditText
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search, container, false)
        toolbar = root.findViewById(R.id.search_toolbar)
        searchEditText = root.findViewById(R.id.edittext_in_fragment_search)
        // this dismisses the keyboard on a click of toolbar back arrow
        toolbar.setNavigationOnClickListener(View.OnClickListener { hideSoftKeyboard(toolbar) })

        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        // two sub-tiles - one for previous searches and one for search matches
        val childFragmentManager = childFragmentManager
        recentSearchesFragment = childFragmentManager.findFragmentById(
            R.id.search_container_recent
        ) as RecentSearchesFragment

        searchMatchesFragment = childFragmentManager.findFragmentById(
            R.id.search_container_matches
        ) as SearchMatchesFragment
        searchMatchesFragment.setCallback(this)
        recentSearchesFragment.setViewModel(searchViewModel)
        searchMatchesFragment.setViewModel(searchViewModel)

        searchEditText.setOnEditorActionListener(this)
        searchEditText.doOnTextChanged { text, start, before, count ->
            //Timber.i("onTextChanged: $text, $start, $before, $count")
            if (text != null) {
                val searchString = text.trim { it <= ' ' }
                startSearch(searchString.toString())
            }
        }

        val clearButton = root.findViewById<ImageView>(R.id.clear_button_in_fragment_search)
        clearButton.setOnClickListener(View.OnClickListener {
            searchEditText.text.clear()
        })

        return root
    }


    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_SEND -> Timber.i("SEND")
            EditorInfo.IME_ACTION_DONE -> Timber.i("DONE")
            EditorInfo.IME_ACTION_SEARCH -> Timber.i("SEARCH")
        }
        return true
    }


            /* override fun onQueryTextSubmit(queryText: String): Boolean {
                 Timber.v("Submit text: $queryText")
                 return true
             }

             override fun onQueryTextChange(queryText: String): Boolean {
 //            searchView.setCloseButtonVisibility(queryText)
                 val searchString = queryText.trim { it <= ' ' }
                 startSearch(searchString, false)
                 return true
             }*/

    override fun onStart() {
        super.onStart()
        showPanel(PANEL_RECENT_SEARCHES)
    }

    override fun clearKeyboard() {
        hideSoftKeyboard(toolbar)
    }

    /**
     * Show a particular panel, which can be one of:
     * - PANEL_RECENT_SEARCHES
     * - PANEL_SEARCH_RESULTS
     * Automatically hides the previous panel.
     *
     * @param panel Which panel to show.
     */
    private fun showPanel(panel: Int) {
        when (panel) {
            PANEL_RECENT_SEARCHES -> {
                searchMatchesFragment.hide()
                recentSearchesFragment.show()
            }
            PANEL_SEARCH_RESULTS -> {
                recentSearchesFragment.hide()
                searchMatchesFragment.show()
            }
            else -> {
            }
        }
    }

    private fun startSearch(term: String) {

        if (TextUtils.isEmpty(term)) {
            showPanel(PANEL_RECENT_SEARCHES)
        } else if (getActivePanel() == PANEL_RECENT_SEARCHES) {
            showPanel(PANEL_SEARCH_RESULTS)
        }

        searchMatchesFragment.startSearch(term)
    }


    private fun getActivePanel(): Int {
        return if (searchMatchesFragment.isShowing()) {
            PANEL_SEARCH_RESULTS
        } else {
            //otherwise, the recent searches must be showing:
            PANEL_RECENT_SEARCHES
        }
    }

    companion object {
        private const val PANEL_RECENT_SEARCHES = 0
        private const val PANEL_SEARCH_RESULTS = 1
    }


}