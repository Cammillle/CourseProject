package com.alfabank.homework.courseproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val secureStorageManager: SecureStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadApiKey()
    }

    private fun loadApiKey() {
        viewModelScope.launch {
            val apiKey = secureStorageManager.getApiKey()
            if (apiKey != null) {
                _uiState.value = MainUiState.Success(apiKey)
            } else {
                _uiState.value = MainUiState.Error("Не удалось загрузить ключ API")
            }
        }
    }

    fun retry() {
        _uiState.value = MainUiState.Loading
        loadApiKey()
    }
}

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val apiKey: String) : MainUiState()
    data class Error(val message: String) : MainUiState()
}