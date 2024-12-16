package com.example.playlistmaker1.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker1.R
import com.example.playlistmaker1.Themes
import com.example.playlistmaker1.ui.library.MediaActivity
import com.example.playlistmaker1.ui.search.SearchActivity
import com.example.playlistmaker1.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Themes.setNightModeState(this, Themes.getNightModeState(this))
        val settings: Button = findViewById(R.id.SettingsButton)
        settings.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
        val search: Button = findViewById(R.id.SearchButton)
        search.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            this.startActivity(intent)
        }
        val media: Button = findViewById(R.id.MediaButton)
        media.setOnClickListener {
            val intent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(intent)
        }
        supportActionBar?.hide()

    }


}
