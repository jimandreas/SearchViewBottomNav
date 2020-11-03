package com.example.searchviewbottomnav.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.searchviewbottomnav.Fruits
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.ui.fruit.FruitActivity
import com.example.searchviewbottomnav.util.PrefsUtil

class SearchResultsFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchResultsDisplay: FrameLayout
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapterInUse: SimpleStringRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_results, container, false)

        searchResultsDisplay = root.findViewById(R.id.search_results_display)
        recyclerView = root.findViewById(R.id.search_results_list)

        val searchSuggestion = root.findViewById<TextView>(R.id.search_suggestion)
        searchSuggestion!!.text = "YOUR RESULTS HERE"

        adapterInUse = SimpleStringRecyclerViewAdapter(root.context, listOf(""))
        adapterInUse.setViewModel(searchViewModel)
        with(recyclerView) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = adapterInUse
        }
        return root
    }

    fun setViewModel(viewModel: SearchViewModel) {
        searchViewModel = viewModel
    }

    fun show() {
        searchResultsDisplay.visibility = View.VISIBLE
    }

    fun hide() {
        searchResultsDisplay.visibility = View.GONE
    }

    fun startSearch(searchTerm: String, force: Boolean) {
        val matches = Fruits.searchFruit(searchTerm)
        val matchNames = matches.map { it.name }
        adapterInUse.updateStringList(matchNames)
        adapterInUse.notifyDataSetChanged()
    }

    class SimpleStringRecyclerViewAdapter(context: Context, private val valuesIn: List<String>)
        : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>() {

        private val typedValue = TypedValue()
        private val backgroundResourceId: Int
        private var stringListToDisplay = valuesIn
        lateinit var searchViewModel: SearchViewModel

        class ViewHolder(val theView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(theView) {
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

        fun getValueAt(position: Int): String {
            return stringListToDisplay[position]
        }

        init {
            context.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
            backgroundResourceId = typedValue.resourceId
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_search_item_textview, parent, false)
            view.setBackgroundResource(backgroundResourceId)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.boundString = stringListToDisplay[position]
            holder.textView.text = stringListToDisplay[position]

            holder.textView.setOnClickListener { v ->
                val context = v.context
                val intent = Intent(context, FruitActivity::class.java)
                val fruitName = holder.boundString
                intent.putExtra(FruitActivity.EXTRA_NAME, fruitName)

                searchViewModel.updateSearchStringList(fruitName)

                val oldSet = PrefsUtil.getStringSet(
                        PrefsUtil.PREVIOUS_SEARCHES_KEY,
                        setOf(""))
                var newSet = mutableSetOf("")
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
}