package com.rubayllo.actividad05.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "com.utad.ayllonaplicacionideas")

class DataStoreManager: LocalStorageRepository {
    /**
     * Incluimos las claves para poder acceder a los datos que necesitamos
     */
    val userNameKey: Preferences.Key<String> = stringPreferencesKey("user_name")
    val passwordKey: Preferences.Key<String> = stringPreferencesKey("user_password")
    val isRegister: Preferences.Key<Boolean> = booleanPreferencesKey("user_register")

    /**
     *  "suspend" para que la función se ejecute en segundo plano. En DataStore es necesario que las
     *  funciones se ejecuten así. Al llevar la notación "suspend" hay que lanzarla dentro de una corrutina.
     *  Una corrutina nos permite lanzar tareas de forma asíncrona en este caso lo ejecutamos en un hilo
     *  secundario que son los que se utilizan para operaciones de lectura y escritura que en Android
     *  se llama IO, ese hilo también se utiliza para hacer peticiones de red.
     */

    /**
     * Para guardar los datos con DataStore, deberemos hacerlo mediante la función “.edit{ }”
     */
    override suspend fun saveUser(context: Context, nameUser: String, password: String, check: Boolean) {
        context.dataStore.edit { editor ->
            editor[userNameKey] = nameUser
            editor[passwordKey] = password
            editor[isRegister] = check
        }
    }

    override suspend fun deleteAll(context: Context) {
        context.dataStore.edit { editor ->
            editor.clear()
        }
    }

    override suspend fun setLoginCheckIn(context: Context) {
        context.dataStore.edit { editor ->
            editor[isRegister] = true
        }
    }


    override suspend fun setLoginCheckOut(context: Context) {
        context.dataStore.edit { editor ->
            editor[isRegister] = false
        }
    }

    /**
     * Flow en Kotlin es un flujo continuo de información a donde tu te puedes suscribir
     * y se realizan secuencias asincrónicas que se encargan de llevar a cabo la emisión
     * de valores de manera secuencial. Con Flow podemos omitir el suspend ya que el flujo
     * ya es asíncrono.
     * Hay que implementar las dependencias de Corroutines en gradle:app
     */
    override fun getIsRegister(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { editor ->
            editor[isRegister] ?: false
        }
    }

    override fun getUser(context: Context): Flow<String> {
        return context.dataStore.data.map { editor ->
            editor[userNameKey] ?: ""
        }
    }

    override fun getPassword(context: Context): Flow<String> {
        return context.dataStore.data.map { editor ->
            editor[passwordKey] ?: ""
        }
    }

    /**
     * He creado la clase de tipo data un poco más abajo para poder crear un objeto con los datos que
     * necesito recuperar en esta función y mandarlos juntos en caso de necesitarlos ya que de otra
     * forma solamente podríamos acceder a ellos de uno en uno
     */
    data class UserProfile(
        val name: String,
        val password: String,
        val isRegister: Boolean
    )

    override fun getAllDataUser(context: Context): Flow<UserProfile> {
        return context.dataStore.data.map { preferences ->
            UserProfile(
                name = preferences[stringPreferencesKey("user_name")].orEmpty(),
                password = preferences[stringPreferencesKey("user_password")].orEmpty(),
                isRegister = preferences[booleanPreferencesKey("user_register")] ?: false
            )
        }
    }
}