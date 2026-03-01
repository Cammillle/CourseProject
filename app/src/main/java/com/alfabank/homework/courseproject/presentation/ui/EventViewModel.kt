package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.data.local.toItem
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    init {
        Log.d("ViewModel", "Instance created: ${hashCode()}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "Instance cleared: ${hashCode()}")
    }

    private val _query = MutableStateFlow(
        Queries(
            city = "spb",
            category = "all"
        )
    )
    val query = _query.asStateFlow()

    //val selectedCategory = MutableStateFlow("all")

    val favouriteItems = repository.getFavouriteEvents()

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentPagingFlow: Flow<PagingData<Item>> = _query.flatMapLatest { query ->
        Log.d("ViewModel", "Accessing category: $query")
        Log.d("ViewModel", "Creating new flow for category: $query")

        // Создаём поток для этой категории и кэшируем его в scope ViewModel
        if (query.category == "all") {
            repository.getEventsWithoutCategory(query.city)
        } else {
            repository.getEventsWithCategory(query.category, query.city)
        }
    }.cachedIn(viewModelScope)


    fun onFavouriteClick(item: Item) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, item.isFavourite)
        }
    }

    fun selectCategory(category: String) {
        if (category != "all") {
            _query.update { queries ->
                queries.copy(category = categoryMap[category]!!)
            }
        } else {
            _query.update { queries ->
                queries.copy(category = category)
            }
        }
    }

    fun selectCity(city: String) {
        _query.update { queries ->
            queries.copy(city = city )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: StateFlow<List<Item>> =
        combine(
            searchQuery.debounce(300L),
            refreshTrigger.onStart { emit(Unit) }
        ) { query, _ -> query }
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    repository.searchEvent(query)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun searchEvent(newQuery: String) {
        _searchQuery.value = newQuery
        Log.d("ViewModel", "Search event $newQuery")
    }

    fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }

    fun refreshSearchList() {
        viewModelScope.launch {
            _searchQuery.value = ""
        }
    }
}

data class Queries(
    val city: String,
    val category: String
)

data class HomeState(
    var events: List<Item> = emptyList(),
    var isLoading: Boolean = false,
    var error: String? = null,
    val nextDataIsLoading: Boolean = false,
    val searchQuery: String = "",
    val category: String = ""
)
