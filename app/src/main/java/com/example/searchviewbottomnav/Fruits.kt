@file:Suppress("UnnecessaryVariable")

package com.example.searchviewbottomnav

import java.util.*

object Fruits {
    data class FruitNameImage(val name: String, val imageurl: String)

    private val fruitList = listOf(
            FruitNameImage(name = "Apple", imageurl = "https://i.imgur.com/qAauMaa.jpg"),
            FruitNameImage(name = "Apricot", imageurl = "https://i.imgur.com/1X568rx.jpg"),
            FruitNameImage(name = "Banana", imageurl = "https://i.imgur.com/SBQGtgo.jpg"),
            FruitNameImage(name = "Blackberry", imageurl = "https://i.imgur.com/GSY81aF.jpg"),
            FruitNameImage(name = "Cantaloupe", imageurl = "https://i.imgur.com/X9lJMWW.png"),
            FruitNameImage(name = "Cherries", imageurl = "https://i.imgur.com/zmCBKgF.jpeg"),
            FruitNameImage(name = "Coconut", imageurl = "https://i.imgur.com/lEwKd9P.jpeg"),
            FruitNameImage(name = "Cranberry", imageurl = "https://i.imgur.com/A7o9fyY.jpg"),
            FruitNameImage(name = "Durian", imageurl = "https://i.imgur.com/Vh9wlXG.jpg"),
            FruitNameImage(name = "Gooseberry", imageurl = "https://i.imgur.com/I62GkU1.jpg"),
            FruitNameImage(name = "Grapefruit", imageurl = "https://i.imgur.com/yokH4Mi.jpeg"),
            FruitNameImage(name = "Grapes", imageurl = "https://i.imgur.com/RlIMrZl.jpeg"),
            FruitNameImage(name = "Kiwifruit", imageurl = "https://i.imgur.com/BbiwKl4.jpg"),
            FruitNameImage(name = "Mulberry", imageurl = "https://i.imgur.com/G8Vratq.jpg"),
            FruitNameImage(name = "Passion Fruit", imageurl = "https://i.imgur.com/uTp9Htn.jpeg"),
            FruitNameImage(name = "Peach", imageurl = "https://i.imgur.com/lHgOEHW.jpg"),
            FruitNameImage(name = "Pear", imageurl = "https://i.imgur.com/l2ocHBz.jpg"),
            FruitNameImage(name = "Pineapple", imageurl = "https://i.imgur.com/fKVwbBM.jpeg"),
            FruitNameImage(name = "Raspberries", imageurl = "https://i.imgur.com/bedTgMQ.png"),
            FruitNameImage(name = "Strawberries", imageurl = "https://i.imgur.com/LtYAZ6E.jpeg"),
            FruitNameImage(name = "Watermelon", imageurl = "https://i.imgur.com/F9HNpLK.jpg")
    )

    /**
     * iterate through the fruits and a list of entries
     * where the name contains the search string
     */
    fun searchFruit(searchTerm: String): List<FruitNameImage> {
        val s = searchTerm.toLowerCase(Locale.ROOT)
        val match = fruitList.filter { it.name.toLowerCase(Locale.ROOT).contains(s) }
        return match
    }

    fun getUrl(searchTerm: String): String {
        val match = fruitList.filter { it.name == searchTerm }
        return if (match.isEmpty()) "" else match[0].imageurl
    }

}