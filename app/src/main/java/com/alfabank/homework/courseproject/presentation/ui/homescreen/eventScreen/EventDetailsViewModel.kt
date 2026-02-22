package com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = EventRepositoryImpl()

    private val _eventState = MutableStateFlow(EventState())
    val eventState = _eventState.asStateFlow()

    init {
        viewModelScope.launch {
            val eventId = savedStateHandle.get<Long>(Screen.KEY_EVENT_ID) ?: return@launch
            getEventById(id = eventId)
        }
    }

    fun getEventById(id: Long) {
        viewModelScope.launch {
            _eventState.value = _eventState.value.copy(
                isLoading = true,
                error = null
            )
            repository.getEventById(id).collect{result ->
                result.fold(
                    onSuccess = { event ->
                        _eventState.value = _eventState.value.copy(
                            isLoading = false,
                            event = event
                        )

                    },
                    onFailure = { error ->
                        _eventState.value = _eventState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                )
            }
        }
    }

}