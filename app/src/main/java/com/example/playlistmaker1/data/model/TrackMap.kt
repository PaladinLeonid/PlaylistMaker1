package com.example.playlistmaker1.data.model

import com.example.playlistmaker1.data.model.Track as DataTrack
import com.example.playlistmaker1.domain.model.Track as DomainTrack

object TrackMap {
    fun map(dataTrack: DataTrack): DomainTrack {
        return DomainTrack(
            trackId = dataTrack.trackId,
            trackName = dataTrack.trackName,
            artistName = dataTrack.artistName,
            trackTimeMillis = dataTrack.trackTimeMillis,
            artworkUrl100 = dataTrack.artworkUrl100,
            collectionName = dataTrack.collectionName,
            releaseDate = dataTrack.releaseDate,
            genre = dataTrack.genre,
            country = dataTrack.country,
            previewUrl = dataTrack.previewUrl
        )
    }
}