package com.example.playlistmaker1.data.implement

import android.media.MediaPlayer
import com.example.playlistmaker1.domain.repository.MediaPlayerRepository

class MediaPlayerImplement : MediaPlayerRepository {
    private var mediaPlayer: MediaPlayer? = null

    override fun preparePlayer(url: String) {
        release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
        }
    }

    override fun play() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}