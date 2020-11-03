package com.example.searchviewbottomnav.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text


    private val _previousSearchStringList = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val previousSearchStringList: LiveData<List<String>> = _previousSearchStringList

    fun updateSearchStringList(newSearchString: String) {

    }
}