package com.example.playlistmaker1.domain.api

interface SettingsInteract {
    fun getTheme(): Boolean
    fun setTheme(darkTheme: Boolean)
}