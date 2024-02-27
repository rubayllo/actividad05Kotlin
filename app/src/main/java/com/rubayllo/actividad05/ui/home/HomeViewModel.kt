package com.rubayllo.actividad05.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubayllo.actividad05.data.network.model.DealsModel
import com.rubayllo.actividad05.data.network.repository.DealsRepository
import com.rubayllo.actividad05.data.network.repository.DealsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUIState(
    val gameList: List<DealsModel>?,
    val errorMessage: String?,
    val isLoading: Boolean,
    val isLoggedOut: Boolean
)

class HomeViewModel : ViewModel() {
    private var _homeUIState: MutableStateFlow<HomeUIState> =
        MutableStateFlow(HomeUIState(null, null, false, false))
    val homeUIState: StateFlow<HomeUIState> get() = _homeUIState

    private val apiRepository: DealsRepository = DealsRepositoryImpl()

    fun getInfoDeals(storeId: Int) {
        _homeUIState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepository.getDataListDeals(storeId)

                if (response.isSuccessful && response.body() != null
                    && response.body()!!.isNotEmpty()
                ) {
                    //Actualizamos con la lista de juegos recibida
                    _homeUIState.update {
                        it.copy(
                            gameList = response.body(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    _homeUIState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Ha habido un error al cargar la petición código: ${response.code()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", "No obtiene datos o no hay conexion a internet")
            }
        }
    }

}
