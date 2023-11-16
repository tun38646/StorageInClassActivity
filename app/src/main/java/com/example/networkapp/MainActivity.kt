package com.example.networkapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.StringBuilder

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save and load comic info automatically when app starts)

private const val COMIC_TITLE_KEY = "comic_title"
private const val COMIC_DESC_KEY = "comic_desc"
private const val COMIC_IMAGE_KEY = "comic_image"

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getPreferences(MODE_PRIVATE)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

        loadComic()

    }

    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url, {showComic(it)}, {
            })
        )
    }

    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)

        saveComic(comicObject.getString("title"), comicObject.getString("alt"), comicObject.getString("img"))
    }

    private fun saveComic(title: String, description: String, image: String) {
        val editor = preferences.edit()
        editor.putString(COMIC_TITLE_KEY, title)
        editor.putString(COMIC_DESC_KEY, description)
        editor.putString(COMIC_IMAGE_KEY, image)
        editor.apply()
    }

    private fun loadComic() {
        titleTextView.text = preferences.getString(COMIC_TITLE_KEY, "")
        descriptionTextView.text = preferences.getString(COMIC_DESC_KEY, "")
        Picasso.get().load(preferences.getString(COMIC_IMAGE_KEY, "")).into(comicImageView)
    }

}