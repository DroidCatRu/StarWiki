package com.example.starwiki.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwiki.data.models.PersonDB

@Dao
interface PersonDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(person: PersonDB)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFilmPersons(personList: List<PersonDB>)

  @Query("SELECT * FROM person_table WHERE personId = :personId")
  suspend fun getPerson(personId: Int): PersonDB?
}