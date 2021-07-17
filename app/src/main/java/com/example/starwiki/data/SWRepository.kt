package com.example.starwiki.data

import android.util.Log
import androidx.lifecycle.LiveData

class SWRepository(val network: SWNetwork, val db: FilmDao) {

  val allFilms: LiveData<List<Film>> = db.getAll()

  suspend fun loadFilms() {
    try {
      val result = network.getAllFilms()
      Log.d("Films", result.results.toString())
      db.clearAll()
      db.insertFilms(result.results)
    } catch (cause: Throwable) {
        throw FilmsRefreshError("Unable to refresh films: ${cause.message}", cause)
    }
  }

  suspend fun getFilmsCount() = db.getFilmsCount()

  private suspend fun insert(film: Film) {
    db.insert(film)
  }

  private suspend fun update(film: Film) {
    db.update(film)
  }

  private suspend fun get(episodeId: Int): Film? {
    return db.get(episodeId)
  }

  private fun find(pattern: String): LiveData<List<Film>> {
    return db.find("%${pattern}%")
  }

  private suspend fun clearAll() {
    db.clearAll()
  }
}

class FilmsRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)