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

package com.example.searchviewbottomnav.ui.fruit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.searchviewbottomnav.Fruits
import com.example.searchviewbottomnav.MainActivity
import com.example.searchviewbottomnav.R

class FruitActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit)
        title = "Your selection here"


        val fruitName = intent.getStringExtra(EXTRA_NAME)
        if (fruitName != null) {
            val url = Fruits.getUrl(fruitName)
            loadBackdrop(url)
            val textView = findViewById<TextView>(R.id.fruit_name_text_view)
            textView!!.text = fruitName
        }
    }

    private fun loadBackdrop(url:String) {
        val imageView: ImageView = findViewById(R.id.backdrop)
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(imageView)

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_NAME = "fruitname"
    }
}