package com.example.starwiki.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_table")
data class PersonDB(
  @PrimaryKey
  val personId: Int,
  val name: String,
  val birthYear: String,
  val eyeColor: String,
  val gender: String,
  val hairColor: String,
  val height: String,
  val mass: String,
  val homeWorld: String
) {
  constructor(personId: Int, person: Person) : this(
    personId,
    person.name,
    person.birthYear,
    person.eyeColor,
    person.gender,
    person.hairColor,
    person.height,
    person.mass,
    person.homeWorld
  )
}
