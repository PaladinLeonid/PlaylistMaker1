package com.example.playlistmaker1

import Track
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory (private val sharedPreferences: SharedPreferences){
    private val historyKey = "search_history"
    private val maxHistorySize = 10
    private val gson = Gson()
    fun getSearchHistory(): List<Track> {
        val jsonString = sharedPreferences.getString(historyKey, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(jsonString, type)
    }
    fun saveToHistory(track: Track) {
        val history = getSearchHistory().toMutableList()
        history.removeAll { it.artistName == track.artistName && it.trackName == track.trackName }
        history.add(0, track)
        if (history.size > maxHistorySize) {
            history.removeAt(maxHistorySize)
        }
        saveHistory(history)
    }
    fun clearHistory() {
        sharedPreferences
            .edit()
            .remove(historyKey)
            .apply()
    }
    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences
            .edit().putString(historyKey, json)
            .apply()
    }
}