package com.example.playlistmaker1.data.network

import com.example.playlistmaker1.data.model.Track

data class TrackResponse (
    val resultCount: Int,
    val results: List<Track>
)
