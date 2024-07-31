package com.example.playlistmaker1

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val actionBar = supportActionBar
        if (actionBar != null) {
            val background = ColorDrawable(resources.getColor(R.color.white))
            actionBar.setBackgroundDrawable(background)
            actionBar.title = "Медиатека"
            actionBar.setDisplayHomeAsUpEnabled(true)

        }
    }
}