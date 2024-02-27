package com.rubayllo.actividad05.data.network.repository

import com.rubayllo.actividad05.data.network.model.DealsModel
import retrofit2.Response

interface DealsRepository {
    suspend fun getDataListDeals(storeId: Int): Response<List<DealsModel>>
}