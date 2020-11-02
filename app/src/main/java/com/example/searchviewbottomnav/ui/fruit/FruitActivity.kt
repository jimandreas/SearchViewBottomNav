package com.example.searchviewbottomnav.ui.fruit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.searchviewbottomnav.MainActivity
import com.example.searchviewbottomnav.R

class FruitActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val fruitName = intent.getStringExtra(EXTRA_NAME)
        val fruitUrl = intent.getStringExtra(EXTRA_URL)

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_NAME = "fruitname"
        const val EXTRA_URL = "fruiturl"
    }
}