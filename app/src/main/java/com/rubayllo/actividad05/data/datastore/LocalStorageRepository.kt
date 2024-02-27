package com.rubayllo.actividad05.data.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface LocalStorageRepository {

    suspend fun saveUser(context: Context, nameUser: String, password: String, check: Boolean)

    suspend fun deleteAll(context: Context)

    suspend fun setLoginCheckIn(context: Context)

    suspend fun setLoginCheckOut(context: Context)

    fun getIsRegister(context: Context): Flow<Boolean>

    fun getUser(context: Context): Flow<String>

    fun getPassword(context: Context): Flow<String>

    fun getAllDataUser(context: Context): Flow<DataStoreManager.UserProfile>
}