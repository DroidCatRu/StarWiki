package com.example.starwiki.filmlist

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwiki.data.FilmsRefreshError
import com.example.starwiki.data.SWRepository
import com.example.starwiki.util.twoArgViewModelFactory
import kotlinx.coroutines.launch


class FilmViewModel(private val repository: SWRepository, private val app: Application) :
  AndroidViewModel(app) {

  var isConnected = false;

  companion object {

    val FACTORY = twoArgViewModelFactory(::FilmViewModel)
  }

  init {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      try {
        val cm = getApplication<Application>().applicationContext
          .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
          override fun onAvailable(network: Network) {
            isConnected = true
            super.onAvailable(network)
          }

          override fun onLost(network: Network) {
            isConnected = false
            super.onLost(network)
          }
        })
      } catch (e: Exception) {
        isConnected = false
      }
    }
  }

  val films = repository.allFilms

  private val _isUpdating = MutableLiveData<Boolean>(false)

  val isUpdating: LiveData<Boolean>
    get() = _isUpdating

  fun loadFilms() = launchDataLoad {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      val cm = getApplication<Application>().applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val activeNetwork = cm.activeNetworkInfo
      isConnected = activeNetwork?.isConnectedOrConnecting == true
    }
    if (isConnected) {
      repository.loadFilms()
    }
    else {
      Toast.makeText(getApplication<Application>().applicationContext,
        "You're offline" , Toast.LENGTH_SHORT).show()
    }
  }

  private fun launchDataLoad(block: suspend () -> Unit): Unit {
    viewModelScope.launch {
      try {
        _isUpdating.postValue(true)
        block()
      } catch (error: FilmsRefreshError) {
        Log.d("Refresh error", error.message!!)
      } finally {
        _isUpdating.postValue(false)
      }
    }
  }
}