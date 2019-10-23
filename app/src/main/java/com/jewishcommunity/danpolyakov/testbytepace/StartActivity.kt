package com.jewishcommunity.danpolyakov.testbytepace

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jewishcommunity.danpolyakov.testbytepace.model.Picture
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.lang.Runnable

class StartActivity : AppCompatActivity() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private var document: Document? = null
    private var linkOk: Boolean = true

    private suspend fun getDocumentAsync(link: String): Deferred<Document> = coroutineScope {
        async {
            Jsoup.connect(link).get()
        }
    }

    private fun loadDocument(link: String) = scope.launch {
        try {
            document = getDocumentAsync(link).await()
            linkOk = true
        } catch (e: Exception) {
            linkOk = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnGo = findViewById<Button>(R.id.button)

        btnGo.setOnClickListener {
            val link = findViewById<EditText>(R.id.editText).text.toString()

            runBlocking {
                loadDocument(link).join()
            }

            if (!linkOk) {
                Toast.makeText(applicationContext, "Bad link", Toast.LENGTH_LONG).show()
            }

            if (document != null) {
                val images = document!!.getElementsByTag("img")
                if (images?.size == 0) {
                    Toast.makeText(applicationContext, "The page does not contain any images!", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, ListActivity::class.java)
                    val list = ArrayList(images!!.map {
                        Picture(it.attr("abs:src"))
                    }.filterNot { x -> x.url == "" })
                    intent.putParcelableArrayListExtra(ListActivity.IMAGE_LIST, list)
                    startActivity(intent)
                }
            }
        }
    }
}
