package com.example.playlistmaker1.data.network

import com.example.playlistmaker1.Constants.BASE_URL_ITUNES
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ITUNES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createApiService(): ItunesApi {
        return retrofit.create(ItunesApi::class.java)
    }
}