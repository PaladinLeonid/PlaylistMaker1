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

        val forwardButtonTxt = findViewById<TextView>(R.id.forwardButtonTxt)

        val supportBtnTxt = findViewById<TextView>(R.id.supportBtnTxt)

        val settingsShareTxt = findViewById<TextView>(R.id.settingsShareTxt)
        supportActionBar?.hide()

        backButton.setOnClickListener {
            finish()
        }

        settingsShareTxt.setOnClickListener {

            Intent().apply {
                val urlLink = getString(R.string.share_link)
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, urlLink) // Текст сообщения
                type = "text/plain" // Тип содержания
                startActivity(Intent.createChooser(this, null));
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
                }
                startActivity(action)
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

       

   


