package com.example.starwiki.filmlist

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwiki.SWApp
import com.example.starwiki.data.FilmsRefreshError
import com.example.starwiki.util.NetworkState
import com.example.starwiki.util.singleArgViewModelFactory
import kotlinx.coroutines.launch


class FilmViewModel(app: SWApp) :
  AndroidViewModel(app) {

  private val repository = getApplication<SWApp>().repository

  companion object {

    val FACTORY = singleArgViewModelFactory(::FilmViewModel)
  }

  val films = repository.allFilms

  val isConnected = NetworkState(app).isConnected

  private var isOnline = false

  private var loadingStatus = LoadingStatus.NotStarted

  private val _isUpdating = MutableLiveData(false)

  val isUpdating: LiveData<Boolean>
    get() = _isUpdating

  private val _toastMessage = MutableLiveData<String?>(null)

  val toastMessage: LiveData<String?>
    get() = _toastMessage

  fun loadFilms() {
    if (isOnline) {
      loadingStatus = LoadingStatus.Loading
      launchDataLoad {
        repository.loadFilms()
      }
    } else {
      _toastMessage.postValue("Films will be loaded later")
      loadingStatus = LoadingStatus.Interrupted
      _isUpdating.value = false
    }
  }

  fun refreshFilms() {
    if (isOnline) {
      loadingStatus = LoadingStatus.Refreshing
      launchDataLoad {
        repository.loadFilms()
      }
    } else {
      _toastMessage.postValue("You're offline")
      loadingStatus = LoadingStatus.NotStarted
      _isUpdating.value = false
    }
  }

  fun onToastShown() {
    _toastMessage.postValue(null)
  }

  fun online() {
    isOnline = true
    if (loadingStatus == LoadingStatus.Interrupted) {
      loadFilms()
    }
  }

  fun offline() {
    isOnline = false
  }

  private fun launchDataLoad(block: suspend () -> Unit) {
    viewModelScope.launch {
      var loadedSuccessfully = true
      try {
        _isUpdating.postValue(true)
        block()
      } catch (error: FilmsRefreshError) {
        loadedSuccessfully = false
        if (loadingStatus == LoadingStatus.Loading) {
          loadingStatus = LoadingStatus.Interrupted
        }
        _toastMessage.postValue(error.message)
      } finally {
        _isUpdating.postValue(false)
        if (loadedSuccessfully) {
          loadingStatus = LoadingStatus.NotStarted
        }
      }
    }
  }
}

enum class LoadingStatus {
  NotStarted,
  Loading,
  Refreshing,
  Interrupted
}