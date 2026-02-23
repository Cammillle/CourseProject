package com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = EventRepositoryImpl()

    private val _state = MutableStateFlow(EventState())
    val eventState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val eventId = savedStateHandle.get<Long>(Screen.KEY_EVENT_ID) ?: return@launch
            observeEventDetails(id = eventId)
        }
    }

    fun observeEventDetails(id: Long) {
        viewModelScope.launch {
            repository.observeEventById(id)
                .collect { result ->
                    result.fold(
                        onSuccess = { item ->
                            _state.value = _state.value.copy(
                                selectedEvent = item,
                                isLoading = false
                            )
                        },
                        onFailure = {
                            _state.value = _state.value.copy(
                                error = it.message,
                                isLoading = false
                            )
                        }
                    )
                }
        }
    }

}