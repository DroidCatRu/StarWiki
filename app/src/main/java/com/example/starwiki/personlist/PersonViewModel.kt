package com.example.starwiki.personlist

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwiki.SWApp
import com.example.starwiki.data.PersonLoadError
import com.example.starwiki.data.models.PersonDB
import com.example.starwiki.util.twoArgViewModelFactory
import kotlinx.coroutines.launch

class PersonViewModel(private val episodeId: Int, app: SWApp) :
  AndroidViewModel(app) {

  private val repository = getApplication<SWApp>().repository

  var persons = repository.filmPersonsResult

  private val _isLoading = MutableLiveData<Boolean>(false)
  val isLoading: LiveData<Boolean>
    get() = _isLoading

  private val _toastMessage = MutableLiveData<String?>(null)

  val toastMessage: LiveData<String?>
    get() = _toastMessage

  init {
    repository.clearFilmPersonsResult()
    loadPersons()
  }

  fun onToastShown() {
    _toastMessage.postValue(null)
  }

  fun loadPersons() {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        repository.getFilmPersons(episodeId)
      } catch (error: PersonLoadError) {
        _toastMessage.postValue(error.message)
      } finally {
        _isLoading.postValue(false)
      }
    }
  }

  companion object {

    val FACTORY = twoArgViewModelFactory(::PersonViewModel)
  }
}