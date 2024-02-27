package com.rubayllo.actividad05.data.network.repository

import com.rubayllo.actividad05.data.network.DealsApi
import com.rubayllo.actividad05.data.network.model.DealsModel
import retrofit2.Response

class DealsRepositoryImpl: DealsRepository {
    override suspend fun getDataListDeals(storeId: Int): Response<List<DealsModel>> {
        return DealsApi.service.getDataListDeals(storeId)
    }
}