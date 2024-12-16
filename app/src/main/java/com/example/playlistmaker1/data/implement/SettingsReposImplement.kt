package com.example.playlistmaker1.data.implement

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker1.Constants.KEY_SWITCH_THEME
import com.example.playlistmaker1.Constants.THEME_PREFERENCE
import com.example.playlistmaker1.domain.repository.SettingsRepository

class SettingsReposImplement(context: Context) : SettingsRepository {

    private val sharedPreferences =
        context.getSharedPreferences(THEME_PREFERENCE, Context.MODE_PRIVATE)

    @Override
    override fun getTheme(): Boolean {

        val isSystemDarkMode =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        if (isSystemDarkMode) {
            return true
        }

        return if (sharedPreferences.contains(KEY_SWITCH_THEME)) {
            sharedPreferences.getBoolean(KEY_SWITCH_THEME, false)
        } else {
            false
        }
    }

    @Override
    override fun setTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPreferences.edit()
            .putBoolean(KEY_SWITCH_THEME, darkTheme)
            .apply()
    }
}