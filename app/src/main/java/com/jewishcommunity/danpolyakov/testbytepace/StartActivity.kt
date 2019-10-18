package com.jewishcommunity.danpolyakov.testbytepace

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jsoup.Jsoup

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val document = Jsoup.connect("https://forum.awd.ru/viewtopic.php?f=1011&t=165935").get()
        

    }
}
