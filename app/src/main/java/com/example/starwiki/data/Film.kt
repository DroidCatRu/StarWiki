package com.example.starwiki.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "films_table")
data class Film(
  @PrimaryKey
  @Json(name = "episode_id")
  var episodeId: Int,
  var title: String,
  var director: String,
  var producer: String,
  @Json(name = "release_date")
  var releaseDate: String
)
