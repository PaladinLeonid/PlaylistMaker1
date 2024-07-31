package com.example.playlistmaker1

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val actionBar = supportActionBar
        if (actionBar != null) {
            val background = ColorDrawable(resources.getColor(R.color.white))
            actionBar.setBackgroundDrawable(background)
            actionBar.title = "Настройки"

            actionBar.setDisplayHomeAsUpEnabled(true)

        }
    }
}