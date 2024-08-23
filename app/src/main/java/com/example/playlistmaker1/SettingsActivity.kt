package com.example.playlistmaker1

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        val forwardButtonImg = findViewById<ImageView>(R.id.forwardButtonImg)

        val forwardButtonTxt = findViewById<TextView>(R.id.forwardButtonTxt)
        val supportBtnImg = findViewById<ImageView>(R.id.supportBtnImg)

        val supportBtnTxt = findViewById<TextView>(R.id.supportBtnTxt)
        val settingsShareImg = findViewById<ImageView>(R.id.settings_share)

        val settingsShareTxt = findViewById<TextView>(R.id.settingsShareTxt)

        settingsShareImg.setOnClickListener { shareNameApp() }
        settingsShareTxt.setOnClickListener { shareNameApp() }

        supportBtnImg.setOnClickListener{supportEmail()}
        supportBtnTxt.setOnClickListener{supportEmail()}

        forwardButtonImg.setOnClickListener{offer()}
        forwardButtonTxt.setOnClickListener{offer()}

        supportActionBar?.hide()
        backButton.setOnClickListener {
            val mainsIntent = Intent(this, MainActivity::class.java)
            startActivity(mainsIntent)

        }

    }

    private fun offer() {
        Intent().apply {
            val url = getString(R.string.offerlink)
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
            startActivity(this)
        }
    }



    private fun supportEmail() {
        val mail = "PaladinPlay@yandex.ru"
        val title = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        val body = "Спасибо разработчикам и разработчицам за крутое приложение!"
        val action = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$mail")
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, body)
    }
        startActivity(action)
    }

    private fun shareNameApp() {
      val urlLink = "https://practicum.yandex.ru/android-developer/"
        val share = Intent().apply{
            action = Intent.ACTION_SEND // Тип экшена
            putExtra(Intent.EXTRA_TEXT, urlLink) // Текст сообщения
            type = "text/plain" // Тип содержания
            startActivity(Intent.createChooser(this, null));
        }

    }
}

       

   


