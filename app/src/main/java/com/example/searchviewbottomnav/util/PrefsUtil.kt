package com.example.searchviewbottomnav.util

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.*

object PrefsUtil {
    var prefsContext : Context? = null

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext)
        val set = prefs.getStringSet(key, defaultValue)
        return if (set == null) null else Collections.unmodifiableSet(set)
    }

    fun setStringSet(key: String?, value: Set<String?>?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext)
        prefs.edit().putStringSet(key, value).apply()
    }

    const val PREVIOUS_SEARCHES_KEY = "psk"
}