package com.example.starwiki.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FilmDao {

  @Query("SELECT count(*) FROM films_table")
  suspend fun getFilmsCount(): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(film: Film)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFilms(films: List<Film>)

  @Update
  suspend fun update(film: Film)

  @Query("SELECT * FROM films_table ORDER BY episodeId ASC")
  fun getAll(): LiveData<List<Film>>

  @Query("SELECT * FROM films_table WHERE episodeId = :episodeId")
  suspend fun get(episodeId: Int): Film?

  @Query("SELECT * FROM films_table WHERE title LIKE :pattern")
  fun find(pattern: String): LiveData<List<Film>>

  @Query("DELETE FROM films_table")
  suspend fun clearAll()
}