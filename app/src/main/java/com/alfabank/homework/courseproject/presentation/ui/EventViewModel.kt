package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.homescreen.FeedScreenEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepositoryImpl()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var observeJob: Job? = null
    private var currentCategory = "all"

    private val categoryMap = mapOf(
        "Концерты" to "concert",
        "Спектакли" to "theater",
        "Экскурсии" to "tour",
        "Ярмарки" to "yarmarki-razvlecheniya-yarmarki",
        "Активный отдых" to "recreation",
        "Выставки" to "exhibition",
        "Фестивали" to "festival"
    )

    init {
        observeCategory("all")
    }

    fun observeCategory(categoryUi: String) {

        val category = categoryMap[categoryUi] ?: "all"
        currentCategory = category

        observeJob?.cancel()

        observeJob = viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                category = category
            )

            repository.observeEvents(category)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            _state.value = _state.value.copy(
                                events = it.events,
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

    fun clearCategory() {
        observeCategory("all")
    }

    fun loadNextEvents() {

        if (_state.value.nextDataIsLoading) return

        viewModelScope.launch {

            _state.value = _state.value.copy(
                nextDataIsLoading = true
            )

            repository.loadNextPage(currentCategory)

            _state.value = _state.value.copy(
                nextDataIsLoading = false
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
