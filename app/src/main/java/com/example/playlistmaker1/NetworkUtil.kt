package com.example.playlistmaker1

import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object NetworkUtil {

    fun isConnected(connectivityManager: ConnectivityManager): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasAnyTransport() == true
    }

    private fun NetworkCapabilities.hasAnyTransport(): Boolean {
        return hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}