package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.SettingsButton)
        button.setOnClickListener {
            val m_intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(m_intent)
        }
        val button2: Button = findViewById(R.id.SearchButton)
        button2.setOnClickListener {
            val m_intent = Intent(this@MainActivity, SearchActivity::class.java)
            this.startActivity(m_intent)
        }
        val button3: Button = findViewById(R.id.MediaButton)
        button3.setOnClickListener {
            val m_intent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(m_intent)
        }
        supportActionBar?.hide()
    }

}