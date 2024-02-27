package com.rubayllo.actividad05.data.network

import com.rubayllo.actividad05.data.network.model.DealsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DealsService {
    @GET("deals")
    suspend fun getDataListDeals(@Query("storeID") storeId: Int): Response<List<DealsModel>>
}