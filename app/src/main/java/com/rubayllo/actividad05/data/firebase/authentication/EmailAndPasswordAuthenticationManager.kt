package com.rubayllo.actividad05.data.firebase.authentication

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class EmailAndPasswordAuthenticationManager: AuthRepository {
    private val auth = Firebase.auth

    override fun isUserLogged(): Boolean {
        val currentUser = auth.currentUser
        // Comprobamos que no haya ningún usuario autentificado, si lo hay es que ya está loggueado
        if (currentUser != null) {
            // User is signed in
            Log.d("FirebaseAuth", "User is signed in")
        }
        // No user is signed in
        return currentUser != null
    }

    override suspend fun signInFirebaseEmailAndPassword(email: String, password: String): Boolean {
        try {
            // Guardamos lo que nos devuelve la funcion "signInWithEmailAndPassword(email, password)" en una variable
            val result = auth.signInWithEmailAndPassword(email, password)
            // Decimos que espere a que termine la función
            result.await()

            // Con isSuccessful comprobamos si la operación ha sido exitosa
            if (result.isSuccessful) {
                Log.d("FirebaseAuth", "User is signed in")
                return true
            } else {
                Log.d("FirebaseAuth", "User is not signed in")
                return false
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error: ${e.message}")
            return false
        }
    }


    override suspend fun createUserFirebaseEmailAndPassword(email: String, password: String): Boolean {
        try {
            // Guardamos lo que nos devuelve la funcion "createUserWithEmailAndPassword(email, password)" en una variable
            val result = auth.createUserWithEmailAndPassword(email, password)
            // Decimos que espere a que termine la función
            result.await()
            // Con isSuccessful comprobamos si la operación ha sido exitosa
            return if (result.isSuccessful) {
                Log.d("FirebaseAuth", "User is signed in")
                true
            } else {
                Log.d("FirebaseAuth", "User is not signed in")
                false
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error: ${e.message}")
            return false
        }
    }


    override fun deleteUserFirebase() {
        val user = auth.currentUser
        user?.delete()
    }

    override fun signOut() {
        auth.signOut()
    }

}

