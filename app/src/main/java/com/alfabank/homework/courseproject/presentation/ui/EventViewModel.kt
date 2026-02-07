package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.domain.model.Event
import com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen.EventState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepositoryImpl()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _eventState = MutableStateFlow(EventState())
    val eventState = _eventState.asStateFlow()

    private var nextUrl: String? = null

    init {
        getTodayPopularEvents()
    }

    fun getTodayPopularEvents() {
        viewModelScope.launch {
            _homeState.value =
                _homeState.value.copy(
                    isLoading = true,
                    error = null,
                    nextDataIsLoading = false
                )
            repository.getTodayPopularEvents().fold(
                onSuccess = { eventsData ->
                    _homeState.value = _homeState.value.copy(
                        isLoading = false,
                        events = eventsData.events,
                        nextDataIsLoading = false
                    )
                    nextUrl = eventsData.nextUrl
                },
                onFailure = { error ->
                    _homeState.value = _homeState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error",
                        nextDataIsLoading = false
                    )
                    Log.e("TAGATG", homeState.value.error.toString())
                }
            )
        }
    }

    fun getEventById(id: Long) {
        viewModelScope.launch {
            _eventState.value = _eventState.value.copy(
                isLoading = true,
                error = null
            )
            repository.getEventById(id).fold(
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

    fun loadEventsByCategories(category: String = "tour") {
        viewModelScope.launch {
            _homeState.value =
                _homeState.value.copy(
                    isLoading = true,
                    error = null,
                    nextDataIsLoading = false
                )
            repository.getEventsByCategory(pageSize = 20, category = category).fold(
                onSuccess = { eventsData ->
                    _homeState.value = _homeState.value.copy(
                        isLoading = false,
                        events = eventsData.events,
                        nextDataIsLoading = false
                    )
                    nextUrl = eventsData.nextUrl
                },
                onFailure = { error ->
                    _homeState.value = _homeState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error",
                        nextDataIsLoading = false
                    )
                    Log.e("TAGATG", homeState.value.error.toString())
                }
            )
        }
    }

    fun loadNextEvents() {
        Log.e("TAGATG", "Load Next")

        val currentNextUrl = nextUrl

        if (currentNextUrl == null || _homeState.value.nextDataIsLoading) {
            Log.e("TAGATG", "Load Next return")
            return
        }

        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(
                nextDataIsLoading = true,
                error = null
            )

            repository.getNextEvents(currentNextUrl).fold(
                onSuccess = { eventData ->
                    nextUrl = eventData.nextUrl
                    val currentEvents = _homeState.value.events
                    val updatedEvents = currentEvents + eventData.events

                    _homeState.value = _homeState.value.copy(
                        events = updatedEvents,
                        nextDataIsLoading = false
                    )
                },
                onFailure = { error ->
                    _homeState.value = _homeState.value.copy(
                        error = error.message ?: "Failed to load more events",
                        nextDataIsLoading = false
                    )
                }
            )
        }
    }

}

data class HomeState(
    var events: List<Event> = emptyList(),
    var selectedEvent: Event? = null,
    var isLoading: Boolean = false,
    var error: String? = null,
    val nextDataIsLoading: Boolean = false
)
