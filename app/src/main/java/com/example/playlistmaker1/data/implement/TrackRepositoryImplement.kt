package com.example.playlistmaker1.data.implement

import android.util.Log
import com.example.playlistmaker1.data.model.TrackMap
import com.example.playlistmaker1.data.network.ItunesApi
import com.example.playlistmaker1.data.network.TrackResponse
import com.example.playlistmaker1.domain.repository.TrackRepository
import retrofit2.Response
import com.example.playlistmaker1.domain.model.Track as DomainTrack

class TrackRepositoryImplement(private val api: ItunesApi) : TrackRepository {
    override fun searchTracks(term: String): List<DomainTrack>? {
        return try {
            val response: Response<TrackResponse> = api.search(term).execute()

            if (response.isSuccessful) {
                response.body()?.results?.map { TrackMap.map(it) }
            } else {
                Log.e("TrackRepositoryImplement", "Error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("TrackRepositoryImplement", "Exception occurred", e)
            null
        }
    }
}