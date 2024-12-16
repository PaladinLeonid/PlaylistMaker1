package com.example.playlistmaker1.domain.implement


import com.example.playlistmaker1.domain.api.SettingsInteract
import com.example.playlistmaker1.domain.repository.SettingsRepository


class SettingsInteractImplement(private val settingsRepository: SettingsRepository) :
    SettingsInteract {
    @Override
    override fun getTheme(): Boolean {
        return settingsRepository.getTheme()
    }

    @Override
    override fun setTheme(darkTheme: Boolean) {
        settingsRepository.setTheme(darkTheme)
    }
}