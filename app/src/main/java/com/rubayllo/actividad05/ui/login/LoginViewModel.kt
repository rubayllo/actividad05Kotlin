package com.rubayllo.actividad05.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubayllo.actividad05.data.datastore.DataStoreManager
import com.rubayllo.actividad05.data.datastore.LocalStorageRepository
import com.rubayllo.actividad05.data.firebase.authentication.EmailAndPasswordAuthenticationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUIState(
    var errorMessage: String?,
    val wasLoginSuccessfully: Boolean,
    val email: String?,
    val password: String?
)

class LoginViewModel : ViewModel() {
    private var _loginUiState: MutableStateFlow<LoginUIState> =
        MutableStateFlow(LoginUIState(null, false, null, null))
    val loginUiState: StateFlow<LoginUIState> get() = _loginUiState

    //    private val firebaseAuth = EmailAndPasswordAuthenticationManager()
    private val firebaseAuth: EmailAndPasswordAuthenticationManager = EmailAndPasswordAuthenticationManager()
    private lateinit var dataStoreManager: LocalStorageRepository

    fun singInFirebaseEmailAndPassword(context: Context, email: String?, password: String?) {
        if (email != null && password != null) {
            viewModelScope.launch(Dispatchers.IO) {
//                val signIn = firebaseAuth.singInFirebaseEmailAndPassword(email, password)
                val signIn = firebaseAuth.signInFirebaseEmailAndPassword(email, password)
                if (signIn) {
                    _loginUiState.update {
                        it.copy(
                            errorMessage = "Bienvenido $email",
                            wasLoginSuccessfully = true
                        )
                    }
                    dataStoreManager.saveUser(context, email, password, true)

                } else {
                    _loginUiState.update {
                        it.copy(
                            wasLoginSuccessfully = false,
                            errorMessage = "Usuario o contraseña incorrectos"
                        )
                    }
                }
            }
        } else {
            _loginUiState.update {
                it.copy(
                    wasLoginSuccessfully = false,
                    errorMessage = "Los campos no pueden estar vacíos"
                )
            }
        }
        restoreLoginUIState()
    }

    fun getAllDataUser(context: Context) {
        if(this::dataStoreManager.isInitialized == false) {
            dataStoreManager = DataStoreManager()
        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getAllDataUser(context).collect() { data ->
                if(data.isRegister) {
                    _loginUiState.update {
                        it.copy(
                            errorMessage = null,
                            wasLoginSuccessfully = false,
                            email = data.name,
                            password = data.password
                        )
                    }
                }
            }
        }
    }

    fun restoreLoginUIState() {
        _loginUiState.update {
            it.copy(
                errorMessage = null,
                wasLoginSuccessfully = false
            )
        }
    }
}


