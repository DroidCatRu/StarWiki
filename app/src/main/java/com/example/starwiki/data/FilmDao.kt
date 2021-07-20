package com.example.starwiki.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.starwiki.data.models.FilmDB

@Dao
interface FilmDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(film: FilmDB)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFilms(films: List<FilmDB>)

  @Update
  suspend fun update(film: FilmDB)

  @get:Query("SELECT * FROM films_table ORDER BY episodeId ASC")
  val allFilms: LiveData<List<FilmDB>>

  @Query("SELECT * FROM films_table WHERE episodeId = :episodeId")
  suspend fun get(episodeId: Int): FilmDB?

  @Query("SELECT * FROM films_table WHERE title LIKE :pattern")
  fun find(pattern: String): LiveData<List<FilmDB>>

  @Query("DELETE FROM films_table")
  suspend fun clearAll()
}