package com.example.playlistmaker1.domain.implement

import com.example.playlistmaker1.domain.api.MediaPlayerInteract
import com.example.playlistmaker1.domain.model.PlayerControl
import com.example.playlistmaker1.domain.model.Track
import com.example.playlistmaker1.domain.repository.MediaPlayerRepository

class MediaPlayerInteractImplement(private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerInteract {
    private val playerControl = PlayerControl()
    override fun play() {
        mediaPlayerRepository.play()
        playerControl.play()
    }
    override fun execute(track: Track) {
        mediaPlayerRepository.preparePlayer(track.previewUrl)
        mediaPlayerRepository.play()
        playerControl.play()
    }
    override fun pause() {
        mediaPlayerRepository.pause()
        playerControl.pause()
    }
    override fun stop() {
        mediaPlayerRepository.release()
        playerControl.stop()
    }
    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }
    override fun isPlaying(): Boolean {
        return playerControl.isPlaying()
    }
}