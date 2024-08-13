package com.example.playlistmaker1

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        supportActionBar?.hide()
        backButton.setOnClickListener {
            val mainsIntent = Intent(this, MainActivity::class.java)
            startActivity(mainsIntent)
        }
        }
    }
