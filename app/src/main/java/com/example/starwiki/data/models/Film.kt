package com.example.starwiki.data.models

import com.squareup.moshi.Json

data class Film(
  @Json(name = "episode_id")
  var episodeId: Int,
  var title: String,
  var director: String,
  var producer: String,
  @Json(name = "release_date")
  var releaseDate: String,
  @Json(name = "characters")
  var persons: List<String>
)