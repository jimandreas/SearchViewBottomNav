package com.example.searchviewbottomnav.ui.search

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.searchviewbottomnav.R

class RecentSearchesFragment : Fragment() {

    private lateinit var recentSearchesContainer : FrameLayout
//    private lateinit var recentSearchesList : ExpandableListView
    private lateinit var recentSearchesList : ListView
    private lateinit var localContext: Context

    private val wiredList   = mutableListOf(
            "apple",
            "orange",
            "banana",
            "grape",
            "kiwifruit",
            "melon",
            "durian",
            "apricot",
            "blueberry",
            "marionberry",
            "blackberry",
            "peach",
            "strawberry",
            "raspberry"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_recent, container, false)
        localContext = root!!.context

        recentSearchesContainer = root.findViewById(R.id.recent_searches_container)
        recentSearchesList = root.findViewById(R.id.recent_searches_list)
        return root
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
                newView = layoutInflater.inflate(R.layout.item_search_recent, null)
            }
            (newView as TextView).text = someList[position]
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
                newView = layoutInflater.inflate(R.layout.item_search_recent, null)
            }
            return newView!!
        }

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
            var newView = convertView
            if (newView == null) {
                val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                newView = layoutInflater.inflate(R.layout.item_search_recent, null)
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