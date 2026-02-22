package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.homescreen.FeedScreenEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepositoryImpl()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private var nextUrl: String? = null

    init {
        getTodayPopularEvents()
    }

    fun onEvent(event: FeedScreenEvent) {
        when (event) {
            is FeedScreenEvent.onCategoryChange -> {
                _homeState.value = _homeState.value.copy(
                    searchQuery = event.category)


            }

            is FeedScreenEvent.onSearchQueryChange -> TODO()
            FeedScreenEvent.onLoadNextData -> TODO()
        }
    }

    fun getTodayPopularEvents(
        query: String = _homeState.value.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            _homeState.value =
                _homeState.value.copy(
                    isLoading = true,
                    error = null,
                    nextDataIsLoading = false
                )
            repository.getEventsWithoutFilters().
            collect{result ->
                result.fold(
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
    }

    fun loadEventsByCategories(
        category: String,
        fetchFromRemote: Boolean = false,
        query: String = ""
    ) {
        val categoryMap = mapOf(
            "Концерты" to "concert",
            "Спектакли" to "theater",
            "Экскурсии" to "tour",
            "Ярмарки" to "yarmarki-razvlecheniya-yarmarki",
            "Активный отдых" to "recreation",
            "Выставки" to "exhibition",
            "Фестивали" to "festival"
        )

        viewModelScope.launch {
            _homeState.value =
                _homeState.value.copy(
                    isLoading = true,
                    error = null,
                    nextDataIsLoading = false
                )
            val category = categoryMap.getValue(category)

            repository.getTodayPopularEventsByCategory(
                category = category,
                query = query
            ).collect { result ->
                result.fold(
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
    var events: List<Item> = emptyList(),
    var isLoading: Boolean = false,
    var error: String? = null,
    val nextDataIsLoading: Boolean = false,
    val searchQuery: String = "",
    val category: String = ""
)
