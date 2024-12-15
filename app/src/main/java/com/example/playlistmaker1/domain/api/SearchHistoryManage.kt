package com.example.playlistmaker1.domain.api

import com.example.playlistmaker1.domain.model.Track

interface SearchHistoryManage {
    fun getSearchHistory(): List<Track>
    fun saveToHistory(track: Track)
    fun clearHistory()
}