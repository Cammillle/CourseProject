package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.homescreen.FeedScreenEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepositoryImpl

    private val categoryMap = mapOf(
        "Концерты" to "concert",
        "Спектакли" to "theater",
        "Экскурсии" to "tour",
        "Ярмарки" to "yarmarki-razvlecheniya-yarmarki",
        "Активный отдых" to "recreation",
        "Выставки" to "exhibition",
        "Фестивали" to "festival"
    )


    private val _selectedCategories = MutableStateFlow<Set<String>>(emptySet())
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()

    val eventsPagingData: Flow<PagingData<Item>> = _selectedCategories.flatMapLatest { categories ->
        val queryId = buildQueryId(categories)
        repository.getEventsPagingData(queryId, categories.toList())
    }.cachedIn(viewModelScope)


    fun observeCategory(category: String) {
        val current = _selectedCategories.value
        _selectedCategories.value = if (category in current) {
            current - category
        } else {
            current + category
        }
    }

    fun clearCategory() {
        _selectedCategories.value = emptySet()
    }

    fun updateCategories(categories: Set<String>) {
        _selectedCategories.value = categories
    }

    fun refresh() {
        viewModelScope.launch {
            val currentQueryId = buildQueryId(_selectedCategories.value)
            repository.clearQueryData(currentQueryId)
            // Тригге перезапуск потока
            _selectedCategories.value = _selectedCategories.value
        }
    }

    private fun buildQueryId(categories: Set<String>): String {
        return if (categories.isEmpty()) "no_filters" else categories.sorted().joinToString(",")
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
