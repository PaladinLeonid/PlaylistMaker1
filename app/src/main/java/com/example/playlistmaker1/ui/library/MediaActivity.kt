package com.example.playlistmaker1.ui.library

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker1.R

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        supportActionBar?.hide()
        backButton.setOnClickListener {
            finish()
        }
    }
}
