/*
 *  Copyright 2021 James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UtilPrefsTest01 {

    private var appContext: Context? = null
    private var prefs: SharedPreferences? = null

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