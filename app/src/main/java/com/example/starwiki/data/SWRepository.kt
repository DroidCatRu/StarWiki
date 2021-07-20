package com.example.starwiki.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.starwiki.data.models.FilmDB
import com.example.starwiki.data.models.PersonDB

class SWRepository(
  val network: SWNetwork,
  val filmDb: FilmDao,
  val filmPersonDb: FilmPersonDao,
  val personDao: PersonDao
) {

  val allFilms: LiveData<List<FilmDB>> = filmDb.allFilms

  private val _filmPersonsResult = MutableLiveData<List<PersonDB>>()
  val filmPersonsResult: LiveData<List<PersonDB>>
    get() = _filmPersonsResult

  suspend fun loadFilms() {
    try {
      val result = network.getAllFilms()

      filmDb.clearAll()
      filmDb.insertFilms(SWResult.getDbFilms(result))

      filmPersonDb.clearFilmPersons()
      filmPersonDb.insertFilmPersons(SWResult.getFilmPersons(result))

    } catch (cause: Throwable) {
      throw FilmsRefreshError("Unable to load films: ${cause.message}", cause)
    }
  }

//  fun findFilm(pattern: String): LiveData<List<FilmDB>> {
//    return filmDb.find("%${pattern}%")
//  }

  suspend fun getFilmPersons(episodeId: Int) {
    val filmPersons = ArrayList<PersonDB>()
    try {
      val personsList = filmPersonDb.getFilmPersons(episodeId)
      for (person in personsList) {
        val personFromDB = personDao.getPerson(person.personId)
        if (personFromDB != null) {
          filmPersons.add(personFromDB)
        } else {
          val personFromServer = network.getPeopleById(person.personId)
          personDao.insert(PersonDB(person.personId, personFromServer))
          filmPersons.add(PersonDB(person.personId, personFromServer))
        }
      }
      _filmPersonsResult.postValue(filmPersons)
    } catch (cause: Throwable) {
      throw PersonLoadError("Unable to load characters: ${cause.message}", cause)
    }
  }

  fun clearFilmPersonsResult() {
    _filmPersonsResult.value = ArrayList()
  }
}

class FilmsRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
class PersonLoadError(message: String, cause: Throwable?) : Throwable(message, cause)