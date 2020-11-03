package com.example.searchviewbottomnav.ui.search

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.searchviewbottomnav.R
import com.example.searchviewbottomnav.ui.fruit.FruitActivity
import com.example.searchviewbottomnav.util.PrefsUtil

class RecentSearchesFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var recentSearchesContainer : FrameLayout
//    private lateinit var recentSearchesList : ExpandableListView
    private lateinit var recentSearchesList : ListView

    private lateinit var localContext: Context

    private var wiredList   = mutableListOf(
            "apple",
            "orange",
            "banana"
    )

    fun addRecentSearchString(searchStringToAdd: String) {
        wiredList.add(1, searchStringToAdd)
    }

    fun clearRecentSearchStringList() {
        wiredList.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_recent, container, false)
        localContext = root!!.context

        recentSearchesContainer = root.findViewById(R.id.recent_searches_container)
        recentSearchesList = root.findViewById(R.id.recent_searches_list)

        val previousSet = PrefsUtil.getStringSet(
                PrefsUtil.PREVIOUS_SEARCHES_KEY,
                setOf(""))
        wiredList.clear()
        if (previousSet != null) {
            wiredList = previousSet.toMutableList()
        }

        val recentSearchesDeleteButton = root.findViewById<ImageView>(R.id.recent_searches_delete_button)
        recentSearchesDeleteButton?.setOnClickListener { _ ->
            wiredList.clear()
        }

        return root
    }

    fun setViewModel(viewModel: SearchViewModel) {
        searchViewModel = viewModel
    }

    fun show() {
        recentSearchesContainer.visibility = View.VISIBLE
    }

    fun hide() {
        recentSearchesContainer.visibility = View.GONE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //val adapter = RecentSearchesAdapter(localContext, ArrayList(wiredList))
        val adapter = RecentSearchesSimpleListAdapter(localContext, ArrayList(wiredList))
        recentSearchesList.adapter = adapter

        searchViewModel.previousSearchStringList.observe(viewLifecycleOwner, {
            val searchList = it
            wiredList = searchList!!.toMutableList()
        })

    }
    // magic layout inflater invocation:
    // val layoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class RecentSearchesSimpleListAdapter(private val context: Context, private val someList: ArrayList<String>)
        : ListAdapter {

        override fun getCount(): Int {
            return someList.size
        }

        override fun getItem(position: Int): Any {
            return someList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var newView = convertView
            if (newView == null) {
                val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                newView = layoutInflater.inflate(R.layout.fragment_search_item_textview, null)
            }
            (newView as TextView).text = someList[position]

            newView.setOnClickListener { v ->
                val context = v.context
                val intent = Intent(context, FruitActivity::class.java)
                val fruitName = (v as TextView).text
                intent.putExtra(FruitActivity.EXTRA_NAME, fruitName)
                context.startActivity(intent)
            }
            return newView
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun isEmpty(): Boolean {
            return someList.isEmpty()
        }

        override fun registerDataSetObserver(observer: DataSetObserver?) {

        }

        override fun unregisterDataSetObserver(observer: DataSetObserver?) {

        }
        override fun hasStableIds(): Boolean {
            return(true)
        }

        override fun isEnabled(position: Int): Boolean {
            return(true)
        }

        override fun areAllItemsEnabled(): Boolean {
            return(true)
        }
    }

    /**
     *  this is a BaseExpandableListAdapter
     *  See https://github.com/bijayy/FirstSearchDemo
     *  for an example
     *  It is not used currently but did work at one time.
     */
    class RecentSearchesAdapter(val context: Context, val someList: ArrayList<String>)
        : BaseExpandableListAdapter() {
        override fun getGroupCount(): Int {
            return 1
        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return someList.size
        }

        override fun getGroup(groupPosition: Int): Any {
            return 0
        }

        override fun getChild(groupPosition: Int, childPosition: Int): Any {
            return someList[childPosition]
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
            var newView = convertView
            if (newView == null) {
                val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                newView = layoutInflater.inflate(R.layout.fragment_search_item_textview, null)
            }
            return newView!!
        }

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
            var newView = convertView
            if (newView == null) {
                val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                newView = layoutInflater.inflate(R.layout.fragment_search_item_textview, null)
            }
            (newView as TextView).text = someList[childPosition]
            return newView
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return true
        }
    }

    fun setCallback(toHere: Callback) {
        callback = toHere
    }

    interface Callback {
        fun switchToSearch(text: String)
    }

    private var callback: Callback? = null
}