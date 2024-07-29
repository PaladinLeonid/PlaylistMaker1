package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Настройки"
            actionBar.setDisplayHomeAsUpEnabled(true)

        }
    }
}