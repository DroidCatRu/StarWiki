package com.example.starwiki.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.starwiki.data.models.FilmPersonDB

@Dao
interface FilmPersonDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(filmPerson: FilmPersonDB)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFilmPersons(filmPersonList: List<FilmPersonDB>)

  @Query("SELECT * FROM filmperson_table WHERE filmId = :episodeId")
  suspend fun getFilmPersons(episodeId: Int): List<FilmPersonDB>

  @Query("SELECT * FROM filmperson_table WHERE personId = :personId")
  suspend fun getPersonFilms(personId: Int): List<FilmPersonDB>

  @Query("DELETE FROM filmperson_table")
  suspend fun clearFilmPersons()
}