package com.example.starwiki

import android.app.Application
import com.example.starwiki.data.SWDatabase
import com.example.starwiki.data.SWRepository
import com.example.starwiki.data.getNetworkService

class SWApp : Application() {

  private lateinit var database: SWDatabase
  lateinit var repository: SWRepository

  override fun onCreate() {
    super.onCreate()
    database = SWDatabase.getInstance(applicationContext)
    repository = SWRepository(
      getNetworkService(),
      database.filmDao,
      database.filmPersonDao,
      database.personDao
    )
  }

}