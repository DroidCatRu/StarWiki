package com.example.starwiki.data.models

import androidx.room.Entity

@Entity(tableName = "filmperson_table", primaryKeys = ["filmId", "personId"])
data class FilmPersonDB(
  val filmId: Int,
  val personId: Int
)
