package com.example.playlistmaker1

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object Themes {

    private const val THEME_PREFERENCES = "theme_preferences"
    private const val THEME_KEY = "theme_key"

    fun getNightModeState(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(THEME_KEY, false)
    }
    fun setNightModeState(context: Context, setNightMode: Boolean) {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(THEME_KEY, setNightMode).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (setNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
