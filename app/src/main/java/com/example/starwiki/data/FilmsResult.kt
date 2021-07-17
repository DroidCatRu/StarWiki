package com.example.starwiki.data

data class FilmsResult(
  var count: Int,
  var next: String?,
  var previous: String?,
  var results: List<Film>
)
