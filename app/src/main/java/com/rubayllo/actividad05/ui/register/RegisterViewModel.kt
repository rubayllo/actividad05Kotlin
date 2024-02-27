package com.rubayllo.actividad05.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubayllo.actividad05.data.datastore.DataStoreManager
import com.rubayllo.actividad05.data.datastore.LocalStorageRepository
import com.rubayllo.actividad05.data.firebase.authentication.EmailAndPasswordAuthenticationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUIState(
    val errorMessage: String?,
    val wasRegisterSuccessfully: Boolean
)

class RegisterViewModel: ViewModel() {
    private var _registerUIState: MutableStateFlow<RegisterUIState> =
        MutableStateFlow(RegisterUIState(null, false))
    val registerUIState: StateFlow<RegisterUIState> get() = _registerUIState

    private val firebaseAuth = EmailAndPasswordAuthenticationManager()
    private lateinit var dataStoreManager: LocalStorageRepository

    fun createFirebaseMailAndPasswordUser(context: Context, email: String, password: String) {
        if(this::dataStoreManager.isInitialized == false) {
            dataStoreManager = DataStoreManager()
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = firebaseAuth.createUserFirebaseEmailAndPassword(email, password)
            if (result) {
                _registerUIState.value = RegisterUIState(null, true)
                dataStoreManager.saveUser(context, email, password, true)
            } else {
                _registerUIState.value = RegisterUIState("Ha habido un error en el registro", false)
            }
        }
    }
}