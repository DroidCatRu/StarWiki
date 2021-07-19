package com.example.starwiki.data

import android.util.Log
import androidx.lifecycle.LiveData

class SWRepository(val network: SWNetwork, val db: FilmDao) {

  val allFilms: LiveData<List<Film>> = db.allFilms

  suspend fun loadFilms() {
    try {
      val result = network.getAllFilms()
      Log.d("Films", result.results.toString())
      db.clearAll()
      db.insertFilms(result.results)
    } catch (cause: Throwable) {
        throw FilmsRefreshError("Unable to load films: ${cause.message}", cause)
    }
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
}

class FilmsRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)