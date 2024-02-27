package com.rubayllo.actividad05.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Necesito:
 * - añadir permisos de internet en el manifest
 * - servicio de retrofit
 * - las respuestas de la api parseadas para el servicio
 */

object DealsApi {

private const val BASE_URL = "https://www.cheapshark.com/api/1.0/"

private val interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val service: DealsService by lazy {
        retrofit.create(DealsService::class.java)
    }
}