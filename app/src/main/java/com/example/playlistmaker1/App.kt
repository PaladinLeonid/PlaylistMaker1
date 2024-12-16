package com.example.playlistmaker1

import android.app.Application
import android.content.Intent
import com.example.playlistmaker1.domain.api.SettingsInteract
import com.example.playlistmaker1.ui.main.MainActivity


class App : Application() {
    private lateinit var settingsInteractor: SettingsInteract
    override fun onCreate() {
        super.onCreate()
        settingsInteractor = Creator.createSettingsInteract(this)
        val themePreference = settingsInteractor.getTheme()
        settingsInteractor.setTheme(themePreference)
        val startActivityMain = Intent(this, MainActivity::class.java)
        startActivityMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startActivityMain)
    }
}