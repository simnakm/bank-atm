package com.example.atmbank

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService
//class for network connection
object NetworkHelper {
    fun isNetworkConnected(context: Context): Boolean {
        var result = false
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //check for android version with sdk version
                result = checkNetworkConnection(this, this.activeNetwork)
            } else {
                val networks = this.allNetworks
                for (network in networks) {
                    if (checkNetworkConnection(this, network)) {
                        result = true
                    }
                }
            }
        }
        return result      
    }
    private fun checkNetworkConnection(
        connectivityManager: ConnectivityManager,
        network: Network?
    ): Boolean {
        connectivityManager.getNetworkCapabilities(network)?.also {
            if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
        return false
    }
}

