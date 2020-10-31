package com.example.searchviewbottomnav.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.searchviewbottomnav.R
import timber.log.Timber

class SearchFragment : Fragment() , RecentSearchesFragment.Callback {

    private lateinit var searchViewModel: SearchViewModel
    private var recentSearchesFragment: RecentSearchesFragment? = null
    private var searchResultsFragment: SearchResultsFragment? = null
    private var searchView : SearchView? = null
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_search, container, false)
        toolbar = root.findViewById(R.id.search_toolbar)
        searchView = root.findViewById(R.id.search_view_in_fragment_search)
        val childFragmentManager = childFragmentManager
        recentSearchesFragment = childFragmentManager.findFragmentById(
                R.id.search_panel_recent) as RecentSearchesFragment?
        recentSearchesFragment!!.setCallback(this)
        searchResultsFragment = childFragmentManager.findFragmentById(
                R.id.fragment_search_results) as SearchResultsFragment?
        toolbar.setNavigationOnClickListener(View.OnClickListener { v: View? -> requireActivity().supportFinishAfterTransition() })


        //initSearchView()
        searchView!!.setOnQueryTextListener(searchQueryListener)

        return root
    }

    private val searchQueryListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(queryText: String): Boolean {
            Timber.v("Submit text: $queryText")
            return true
        }

        override fun onQueryTextChange(queryText: String): Boolean {
//            searchView.setCloseButtonVisibility(queryText)
            val searchString = queryText.trim { it <= ' ' }
            Timber.v("Query text chaange: $searchString")
            return true
        }
    }

    override fun onStart() {
        super.onStart()
        showPanel(PANEL_RECENT_SEARCHES)
    }

    override fun switchToSearch(text: String) {
        TODO("Not yet implemented")
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
                searchResultsFragment!!.hide()
                recentSearchesFragment!!.show()
            }
            PANEL_SEARCH_RESULTS -> {
                recentSearchesFragment!!.hide()
                searchResultsFragment!!.show()
            }
            else -> {
            }
        }
    }

    companion object {
        private const val PANEL_RECENT_SEARCHES = 0
        private const val PANEL_SEARCH_RESULTS = 1
    }
}