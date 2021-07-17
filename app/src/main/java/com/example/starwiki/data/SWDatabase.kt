package com.example.starwiki.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class SWDatabase : RoomDatabase() {

  abstract val filmDao: FilmDao

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