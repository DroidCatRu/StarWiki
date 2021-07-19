package com.example.starwiki.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FilmDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(film: Film)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFilms(films: List<Film>)

  @Update
  suspend fun update(film: Film)

  @get:Query("SELECT * FROM films_table ORDER BY episodeId ASC")
  val allFilms: LiveData<List<Film>>

  @Query("SELECT * FROM films_table WHERE episodeId = :episodeId")
  suspend fun get(episodeId: Int): Film?

  @Query("SELECT * FROM films_table WHERE title LIKE :pattern")
  fun find(pattern: String): LiveData<List<Film>>

  @Query("DELETE FROM films_table")
  suspend fun clearAll()
}