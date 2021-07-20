package com.example.starwiki.data.models

import com.squareup.moshi.Json

data class Person(
  val name: String,
  @Json(name = "birth_year")
  val birthYear: String,
  @Json(name = "eye_color")
  val eyeColor: String,
  val gender: String,
  @Json(name = "hair_color")
  val hairColor: String,
  val height: String,
  val mass: String,
  @Json(name = "homeworld")
  val homeWorld: String
)
