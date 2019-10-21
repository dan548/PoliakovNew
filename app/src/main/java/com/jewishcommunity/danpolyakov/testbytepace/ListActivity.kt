package com.jewishcommunity.danpolyakov.testbytepace

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jewishcommunity.danpolyakov.testbytepace.model.Picture
import com.squareup.picasso.Picasso

class ListActivity : AppCompatActivity() {

    companion object {
        const val IMAGE_LIST = "image list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val pictures = intent.getParcelableArrayListExtra<Picture>(IMAGE_LIST)
        val imageView = findViewById<RecyclerView>(R.id.image_list)
        imageView.layoutManager = LinearLayoutManager(this)
        imageView.adapter = ListAdapter(pictures)
    }

    class ListAdapter(private val values : List<Picture>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun getItemCount() = values.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val myHolder = holder as ViewHolder

            val image = Picasso.get().load(values[position].url)
            image.into(myHolder.imageView)
            myHolder.urlView?.text = values[position].url

            val t = Thread {
                val getImg = image.get()

                val width = getImg.width
                val height = getImg.height

                myHolder.resolutionView?.text = "${width} x ${height}"
                val sizeKb = getImg.byteCount / 1000
                myHolder.sizeView?.text = "${sizeKb}KB"
            }
            t.start()
            t.join()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageView: ImageView? = null
            var urlView: TextView? = null
            var resolutionView: TextView? = null
            var sizeView: TextView? = null
            init {
                imageView = itemView.findViewById(R.id.image_list_item)
                urlView = itemView.findViewById(R.id.url_list_item)
                resolutionView = itemView.findViewById(R.id.resolution_list_item)
                sizeView = itemView.findViewById(R.id.size_list_item)
            }
        }
    }
}
