package com.example.playlistmaker1
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker1.data.implement.MediaPlayerImplement
import com.example.playlistmaker1.data.implement.SearchHistoryImplemet
import com.example.playlistmaker1.data.implement.SettingsReposImplement
import com.example.playlistmaker1.data.implement.TrackRepositoryImplement
import com.example.playlistmaker1.data.network.ItunesApi
import com.example.playlistmaker1.data.network.Retrofit
import com.example.playlistmaker1.domain.api.MediaPlayerInteract
import com.example.playlistmaker1.domain.api.SearchHistoryManage
import com.example.playlistmaker1.domain.api.SettingsInteract
import com.example.playlistmaker1.domain.implement.MediaPlayerInteractImplement
import com.example.playlistmaker1.domain.implement.SearchHistoryManageImplement
import com.example.playlistmaker1.domain.implement.SearchTrackInteractImplement
import com.example.playlistmaker1.domain.implement.SettingsInteractImplement
import com.example.playlistmaker1.domain.repository.SearchHistoryRepository
import com.example.playlistmaker1.domain.repository.TrackRepository


object Creator {

    fun createPlayer(): MediaPlayerInteract {
        val mediaPlayerRepository = MediaPlayerImplement()
        return MediaPlayerInteractImplement(mediaPlayerRepository)
    }

    private fun createSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryImplemet(sharedPreferences)
    }

    fun createManageSearchHistoryUseCase(sharedPreferences: SharedPreferences): SearchHistoryManage {
        val repository = createSearchHistoryRepository(sharedPreferences)
        return SearchHistoryManageImplement(repository)
    }

    private val apiService: ItunesApi = Retrofit.createApiService()

    private fun createTrackRepository(): TrackRepository {
        return TrackRepositoryImplement(apiService)
    }

    fun createSearchTracksUseCase(): SearchTrackInteractImplement {
        val repository = createTrackRepository()
        return SearchTrackInteractImplement(repository)
    }

    fun createSettingsInteract(context: Context): SettingsInteract {
        return SettingsInteractImplement(SettingsReposImplement(context))
    }
}