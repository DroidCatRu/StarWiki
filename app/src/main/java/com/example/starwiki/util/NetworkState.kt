package com.example.starwiki.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkState(context: Context) {

  private var cm: ConnectivityManager = context
    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private var networkRequest: NetworkRequest = NetworkRequest.Builder()
    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
    .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
    .build()

  private val _isConnected = MutableLiveData(false)

  val isConnected: LiveData<Boolean>
    get() = _isConnected

  init {
    try {
      cm.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
          _isConnected.postValue(true)
          super.onAvailable(network)
        }

        override fun onLost(network: Network) {
          _isConnected.postValue(false)
          super.onLost(network)
        }

        override fun onUnavailable() {
          _isConnected.postValue(false)
          super.onUnavailable()
        }
      })
    } catch (e: Exception) {
      _isConnected.postValue(false)
    }
  }

  companion object {

    private lateinit var INSTANCE: NetworkState

    fun getInstance(context: Context): NetworkState {
      if (!::INSTANCE.isInitialized) {
        INSTANCE = NetworkState(context.applicationContext)
      }
      return INSTANCE
    }
  }
}