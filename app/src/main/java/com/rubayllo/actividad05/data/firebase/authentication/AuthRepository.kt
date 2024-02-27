package com.rubayllo.actividad05.data.firebase.authentication

interface AuthRepository {

    fun isUserLogged(): Boolean

    suspend fun createUserFirebaseEmailAndPassword(email: String, password: String): Boolean

    suspend fun signInFirebaseEmailAndPassword(email: String, password: String): Boolean

    fun deleteUserFirebase()

    fun signOut()

}