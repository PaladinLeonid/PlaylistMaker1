package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Медиатека"
            actionBar.setDisplayHomeAsUpEnabled(true)

        }
    }
}