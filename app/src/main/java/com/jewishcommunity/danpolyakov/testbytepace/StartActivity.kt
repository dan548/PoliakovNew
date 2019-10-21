package com.jewishcommunity.danpolyakov.testbytepace

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jewishcommunity.danpolyakov.testbytepace.model.Picture
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnGo = findViewById<Button>(R.id.button)

        val t = Runnable {

            var document: Document? = null
            var isUrlOk = true
            val link: String

            try {
                document = Jsoup.connect(link).get()
            } catch (e: IOException) {
                isUrlOk = false
            }

            runOnUiThread {
                if (isUrlOk) {
                    val images = document?.getElementsByTag("img")
                    if (images?.size == 0) {
                        Toast.makeText(applicationContext, "The page does not contain any images!", Toast.LENGTH_LONG).show()
                    } else {
                        val intent = Intent(this, ListActivity::class.java)
                        val list = ArrayList(images!!.map {
                            Picture(it.attr("abs:src"))
                        })
                        intent.putParcelableArrayListExtra(ListActivity.IMAGE_LIST, list)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(applicationContext, "Incorrect link!", Toast.LENGTH_LONG).show()
                }
            }
        }



        btnGo.setOnClickListener {
            val link = findViewById<EditText>(R.id.editText).text.toString()
            val thread = Thread(t, link)
            thread.start()
        }
    }
}
