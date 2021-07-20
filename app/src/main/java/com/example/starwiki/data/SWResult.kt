package com.example.starwiki.data

import android.util.Log
import com.example.starwiki.data.models.Film
import com.example.starwiki.data.models.FilmDB
import com.example.starwiki.data.models.FilmPersonDB

data class SWResult<T>(
  var count: Int,
  var next: String?,
  var previous: String?,
  var results: List<T>
) {

  companion object {

    fun getDbFilms(result: SWResult<Film>): List<FilmDB> {
      val filmList = ArrayList<FilmDB>()
      for (film in result.results) {
        filmList.add(FilmDB(film))
      }
      return filmList
    }

    fun getFilmPersons(result: SWResult<Film>): List<FilmPersonDB> {
      val filmPersonList = ArrayList<FilmPersonDB>()
      for (film in result.results) {
        for (personUrl in film.persons) {
          val matchResult = """https://swapi.dev/api/people/(\d+)/""".toRegex().find(personUrl)
          if (matchResult != null && matchResult.groupValues.size == 2) {
            val personId = matchResult.groupValues.drop(1)[0].toInt()
            filmPersonList.add(FilmPersonDB(film.episodeId, personId))
          }
        }
      }
      return filmPersonList
    }
  }
}
