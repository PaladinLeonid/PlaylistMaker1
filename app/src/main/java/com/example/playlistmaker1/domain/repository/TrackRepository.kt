package com.example.playlistmaker1.domain.repository

import com.example.playlistmaker1.domain.model.Track

interface TrackRepository {
    fun searchTracks(term: String): List<Track>?
}