package com.example.playlistmaker1.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker1.Creator
import com.example.playlistmaker1.R
import com.example.playlistmaker1.domain.api.SettingsInteract

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteract: SettingsInteract
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.arrow_back)

        val forwardButtonTxt = findViewById<TextView>(R.id.forwardButtonTxt)

        val supportBtnTxt = findViewById<TextView>(R.id.supportBtnTxt)
        val settingsShareTxt = findViewById<TextView>(R.id.settingsShareTxt)
        supportActionBar?.hide()

        backButton.setOnClickListener {
            finish()
        }
        settingsInteract = Creator.createSettingsInteract(this)

        val switchTheme = findViewById<SwitchCompat>(R.id.switchTheme)
        val themePreference = settingsInteract.getTheme()
        switchTheme.isChecked = themePreference

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsInteract.setTheme(isChecked)
            switchTheme.isChecked = isChecked

        settingsShareTxt.setOnClickListener {
            Intent().apply {
                val urlLink = getString(R.string.share_link)
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, urlLink) // Текст сообщения
                type = "text/plain" // Тип содержания
                startActivity(Intent.createChooser(this, null));
            }
        }

        supportBtnTxt.setOnClickListener {
            val mail = getString(R.string.my_email)//"PaladinPlay@yandex.ru"
            val title =
                getString(R.string.support_email_title)//"Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val body =
                getString(R.string.support_email_text)//"Спасибо разработчикам и разработчицам за крутое приложение!"
            val action = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, body)
                startActivity(this)
            }
        }

        forwardButtonTxt.setOnClickListener {
            Intent().apply {
                val url = getString(R.string.offerlink)
                action = Intent.ACTION_VIEW
                data = Uri.parse(url)
                startActivity(this)
            }
        }
        }
    }
}


