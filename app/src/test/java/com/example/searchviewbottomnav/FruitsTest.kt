package com.example.searchviewbottomnav

import com.example.searchviewbottomnav.Fruits.searchFruit
import com.example.searchviewbottomnav.Fruits.getUrl
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

internal class FruitsTest {

    @Test
    @DisplayName("test substring search of fruit list")
    fun searchFruitTest() {
        val aterMelon = searchFruit("AterMelON")
        assertEquals(1, aterMelon.size)
        assertEquals("Watermelon", aterMelon[0].name)
    }

    @Test
    fun getURLTest() {
        val watermelon = getUrl("Watermelon")
        assertEquals("https://i.imgur.com/F9HNpLK.jpg", watermelon)
    }
}