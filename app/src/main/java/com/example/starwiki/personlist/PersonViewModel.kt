package com.example.starwiki.personlist

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwiki.SWApp
import com.example.starwiki.data.PersonLoadError
import com.example.starwiki.util.LoadingStatus
import com.example.starwiki.util.NetworkState
import com.example.starwiki.util.twoArgViewModelFactory
import kotlinx.coroutines.launch

class PersonViewModel(private val episodeId: Int, app: SWApp) :
  AndroidViewModel(app) {

  private val repository = getApplication<SWApp>().repository

  init {
    repository.clearFilmPersonsResult()
  }

  var persons = repository.filmPersonsResult

  val isConnected = NetworkState(app).isConnected

  private var isOnline = false

  private var loadingStatus = LoadingStatus.NotStarted
  private val _isLoading = MutableLiveData(false)

  val isLoading: LiveData<Boolean>
    get() = _isLoading

  private val _toastMessage = MutableLiveData<String?>(null)

  val toastMessage: LiveData<String?>
    get() = _toastMessage

  fun onToastShown() {
    _toastMessage.postValue(null)
  }

  fun loadPersons() {
    loadingStatus = LoadingStatus.Loading
    launchDataLoad {
      repository.getFilmPersons(episodeId)
    }
  }

  fun refreshPersons() {
    loadingStatus = LoadingStatus.Refreshing
    launchDataLoad {
      repository.getFilmPersons(episodeId)
    }
  }

  fun online() {
    isOnline = true
    if (loadingStatus == LoadingStatus.Interrupted) {
      loadPersons()
    }
  }

  fun offline() {
    isOnline = false
  }

  private fun launchDataLoad(block: suspend () -> Unit) {
    viewModelScope.launch {
      var loadedSuccessfully = true
      try {
        _isLoading.postValue(true)
        block()
      } catch (error: PersonLoadError) {
        loadedSuccessfully = false
        if (loadingStatus == LoadingStatus.Loading) {
          loadingStatus = LoadingStatus.Interrupted
        }
        _toastMessage.postValue(error.message)
      } finally {
        _isLoading.postValue(false)
        if (loadedSuccessfully) {
          loadingStatus = LoadingStatus.NotStarted
        }
      }
    }
  }

  companion object {

    val FACTORY = twoArgViewModelFactory(::PersonViewModel)
  }
}