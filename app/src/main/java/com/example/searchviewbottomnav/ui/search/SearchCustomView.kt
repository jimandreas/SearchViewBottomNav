@file:Suppress("MemberVisibilityCanBePrivate")

package com.example.searchviewbottomnav.ui.search

import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import com.example.searchviewbottomnav.R
import java.util.*

/** [SearchView] that exposes contextual action bar callbacks.  */
class SearchCustomView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = /*R.attr.searchViewStyle*/androidx.appcompat.R.attr.searchViewStyle
) : SearchView(
        context!!, attrs, defStyleAttr
) {
    private val searchCloseBtn: ImageView
    private var searchSrcTextView: SearchAutoComplete? = null
    private fun addFilter(textView: TextView, filter: InputFilter) {
        val filters = textView.filters
        val newFilters = Arrays.copyOf(filters, filters.size + 1)
        newFilters[filters.size] = filter
        textView.filters = newFilters
    }

    fun selectAllQueryTexts() {
        searchSrcTextView!!.selectAll()
    }

    fun setSearchHintTextColor(color: Int) {
        searchSrcTextView!!.setHintTextColor(color)
    }

    fun setCloseButtonVisibility(searchString: String?) {
        if (TextUtils.isEmpty(searchString)) {
            searchCloseBtn.visibility = GONE
            searchCloseBtn.setImageDrawable(null)
        } else {
            searchCloseBtn.visibility = VISIBLE
            searchCloseBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_message_24px
                    )
            )
        }
    }

    companion object {
        private const val SEARCH_TEXT_SIZE = 16
    }

    init {

        val whiteColor = resources.getColor(R.color.white)
        searchSrcTextView = findViewById(R.id.search_src_text)
        searchSrcTextView!!.setTextColor(whiteColor)
        searchSrcTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, SEARCH_TEXT_SIZE.toFloat())
        searchSrcTextView!!.setHintTextColor(whiteColor)
        val searchMagIcon = findViewById<ImageView>(R.id.search_mag_icon)
        searchMagIcon.setColorFilter(whiteColor)
        searchCloseBtn = findViewById(R.id.search_close_btn)
        searchCloseBtn.visibility = GONE
        searchCloseBtn.setColorFilter(whiteColor)
        //FeedbackUtil.setButtonLongPressToast(searchCloseBtn)

    }
}