@file:Suppress("RedundantSamConstructor")

package com.example.searchviewbottomnav.ui.settings

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.searchviewbottomnav.R
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {


    private var themePreference: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val context = preferenceScreen.context

        themePreferenceHandler()

//        val versionPreference = findPreference("app_version")
//        val currentVersionString = BuildConfig.VERSION_NAME
//        versionPreference.summary = currentVersionString
    }

    private fun themePreferenceHandler() {
        themePreference = findPreference(getString(R.string.prefs_theme_key))
        if (Build.VERSION.SDK_INT >= 29) {
            themePreference?.entryValues = resources.getStringArray(R.array.theme_array_system_default)
            themePreference?.entries = resources.getStringArray(R.array.theme_array_system_default)
        } else {
            themePreference?.entries = resources.getStringArray(R.array.theme_array_battery_saver)
            themePreference?.entryValues= resources.getStringArray(R.array.theme_array_battery_saver)
        }
        /*themePreference?.summaryProvider =
            Preference.SummaryProvider<ListPreference> { preference ->
                "asdf asdf"

        }*/
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == themePreference?.key) {
            var themeArray: Array<String> = resources.getStringArray(R.array.theme_array_battery_saver)
            if (Build.VERSION.SDK_INT >= 29) {
                themeArray = resources.getStringArray(R.array.theme_array_system_default)
            }
            val theme = sharedPreferences?.getString(key, "ASDF ASDF")

            when (theme) {
                themeArray[0] -> Timber.e("Array 0")
                themeArray[1] -> Timber.e("Array 1")
                themeArray[2] -> Timber.e("Array 2")
            }
            //Timber.e("changed!! key $key at index $index")
        }

    }

    /*
    *
    * fun getThemeAndDayNightModeNoActionBar(): Pair<Int, Int> {
        val appThemesValues = context.resources.getStringArray(R.array.app_themes_values)

        when (getDefaultSharedPreferences().getString(Constants.PREFERENCE_APP_THEME, appThemesValues[0])) {
            appThemesValues[0] ->
                return Pair(R.style.AppTheme_NoActionBar, AppCompatDelegate.MODE_NIGHT_NO)

            appThemesValues[1] ->
                return Pair(R.style.AppTheme_NoActionBar, AppCompatDelegate.MODE_NIGHT_YES)

            appThemesValues[2] ->
                return Pair(R.style.AppTheme_Black_NoActionBar, AppCompatDelegate.MODE_NIGHT_YES)
        }

        return Pair(-1, -1)
    }
    * */
}
