package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Поиск"
            actionBar.setDisplayHomeAsUpEnabled(true)

        }
    }
}