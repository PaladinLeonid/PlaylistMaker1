package com.example.playlistmaker1.domain.repository

interface SettingsRepository {
    fun getTheme(): Boolean
    fun setTheme(darkTheme: Boolean)

}