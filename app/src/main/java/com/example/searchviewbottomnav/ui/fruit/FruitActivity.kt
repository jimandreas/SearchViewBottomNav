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