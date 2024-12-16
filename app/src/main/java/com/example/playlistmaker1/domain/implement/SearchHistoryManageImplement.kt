package com.example.playlistmaker1.domain.implement

import com.example.playlistmaker1.domain.api.SearchHistoryManage
import com.example.playlistmaker1.domain.model.Track
import com.example.playlistmaker1.domain.repository.SearchHistoryRepository

class SearchHistoryManageImplement(private val repository: SearchHistoryRepository) :

    SearchHistoryManage {
    override fun getSearchHistory(): List<Track> {
        return repository.getSearchHistory()
    }

    override fun saveToHistory(track: Track) {
        repository.saveToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}