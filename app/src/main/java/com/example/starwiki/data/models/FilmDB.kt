package com.example.starwiki.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "films_table")
data class FilmDB(
  @PrimaryKey
  var episodeId: Int,
  var title: String,
  var director: String,
  var producer: String,
  var releaseDate: String
): Serializable {

  constructor(film: Film) : this(
    film.episodeId,
    film.title,
    film.director,
    film.producer,
    film.releaseDate
  )
}
