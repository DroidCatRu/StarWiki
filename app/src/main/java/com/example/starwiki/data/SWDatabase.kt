package com.example.starwiki.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.starwiki.data.models.FilmDB
import com.example.starwiki.data.models.FilmPersonDB
import com.example.starwiki.data.models.PersonDB

@Database(entities = [FilmDB::class, FilmPersonDB::class, PersonDB::class], version = 2, exportSchema = false)
abstract class SWDatabase : RoomDatabase() {

  abstract val filmDao: FilmDao
  abstract val filmPersonDao: FilmPersonDao
  abstract val personDao: PersonDao

  companion object {

    private lateinit var INSTANCE: SWDatabase

    fun getInstance(context: Context): SWDatabase {
      synchronized(this) {
        if (!::INSTANCE.isInitialized) {
          INSTANCE = Room
            .databaseBuilder(
              context.applicationContext,
              SWDatabase::class.java,
              "starwars_database"
            )
            .fallbackToDestructiveMigration()
            .build()
        }
      }
      return INSTANCE
    }
  }
}