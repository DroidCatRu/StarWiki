package com.example.starwiki.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.starwiki.R
import com.example.starwiki.data.models.FilmDB
import com.example.starwiki.data.models.PersonDB

@BindingAdapter("personGender")
fun TextView.setPersonGender(person: PersonDB?) {
  person?.let {
    text = resources.getString(R.string.personGender, person.gender)
  }
}

@BindingAdapter("personBirthYear")
fun TextView.setPersonBirthYear(person: PersonDB?) {
  person?.let {
    text = resources.getString(R.string.personBirthYear, person.birthYear)
  }
}

@BindingAdapter("filmProducer")
fun TextView.setFilmProducer(film: FilmDB?) {
  film?.let {
    text = resources.getString(R.string.filmProducer, film.producer)
  }
}

@BindingAdapter("filmDirector")
fun TextView.setFilmDirector(film: FilmDB?) {
  film?.let {
    text = resources.getString(R.string.filmDirector, film.director)
  }
}

@BindingAdapter("filmReleaseDate")
fun TextView.setFilmReleaseDate(film: FilmDB?) {
  film?.let {
    text = resources.getString(R.string.filmReleaseDate, film.releaseDate)
  }
}