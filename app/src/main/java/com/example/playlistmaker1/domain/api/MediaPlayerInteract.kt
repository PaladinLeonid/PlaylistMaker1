package com.example.playlistmaker1.domain.api

import com.example.playlistmaker1.domain.model.Track

interface MediaPlayerInteract {
    fun play()
    fun execute(track: Track)
    fun pause()
    fun stop()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}