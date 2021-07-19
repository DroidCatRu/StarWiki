package com.example.starwiki.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://swapi.dev/api/"

private val service: SWNetwork by lazy {

  val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  val okHttpClient = OkHttpClient.Builder()
    .readTimeout(40, TimeUnit.SECONDS)
    .build()

  val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  retrofit.create(SWNetwork::class.java)
}

fun getNetworkService() = service

interface SWNetwork {

  @GET("films")
  suspend fun getAllFilms(): FilmsResult
}
