package com.example.playlistmaker1.domain.model

data class PlayerControl(var currentState: PlayerStatus = PlayerStatus.PREPARED
) {
    fun play() {
        currentState = PlayerStatus.PLAYING
    }

    fun pause() {
        currentState = PlayerStatus.PAUSED
    }

    fun stop() {
        currentState = PlayerStatus.PAUSED
    }

    fun isPlaying(): Boolean {
        return currentState == PlayerStatus.PLAYING
    }

}