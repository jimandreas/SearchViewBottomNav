package com.example.searchviewbottomnav.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.searchviewbottomnav.util.PrefsUtil.getStringSet
import com.example.searchviewbottomnav.util.PrefsUtil.setStringSet
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UtilPrefsTest01 {

    private var appContext: Context? = null
    private var prefs: SharedPreferences? = null

    val testList = listOf(
        "apple",
        "orange",
        "banana"
    )

    @Before
    fun before() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext)
        assertEquals("com.example.searchviewbottomnav", appContext!!.packageName)

        prefs = PreferenceManager.getDefaultSharedPreferences(appContext)
        assertNotNull(prefs)
    }


    @Test
    fun basicEditTest() {
        PrefsUtil.prefsContext = appContext
        val previousSearches = setOf("asdf", "qwer", "zxcv")
        setStringSet("previousSearches", previousSearches)

        val savedResult = getStringSet("previousSearches", setOf(""))
        assertEquals(savedResult, previousSearches)
    }

}