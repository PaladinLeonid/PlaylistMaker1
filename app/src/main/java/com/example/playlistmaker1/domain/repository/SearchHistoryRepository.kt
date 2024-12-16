package com.example.playlistmaker1.domain.repository

import com.example.playlistmaker1.domain.model.Track

interface SearchHistoryRepository {
    fun getSearchHistory(): List<Track>
    fun saveToHistory(track:Track)
    fun clearHistory()
}