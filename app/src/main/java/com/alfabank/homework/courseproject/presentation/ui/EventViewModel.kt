package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.data.local.BookmarkRepositoryImpl
import com.alfabank.homework.courseproject.data.local.EventEntity
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.homescreen.FeedScreenEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.reflect.Array.set

class EventViewModel : ViewModel() {
    private val repository = EventRepositoryImpl
    private val bookmarkRepository = BookmarkRepositoryImpl
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

    private val bookmarkedFlow = bookmarkRepository.getBookmarks()

    val selectedCategory = MutableStateFlow("all")

    private val pagingDataCache = mutableMapOf<String, Flow<PagingData<Item>>>()
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val currentPagingFlow: Flow<PagingData<Item>> = selectedCategory.flatMapLatest { category ->
        Log.d("ViewModel", "Accessing category: $category")

        Log.d("ViewModel", "Creating new flow for category: $category")

        // Создаём поток для этой категории и кэшируем его в scope ViewModel
        val baseFlow = if (category == "all") {
            repository.getEventsWithoutCategory()
        } else {
            repository.getEventsWithCategory(category)
        }.cachedIn(viewModelScope)

        combine(baseFlow, bookmarkedFlow) { pagingData, bookmark ->
            pagingData.map { item ->
                item.copy(isBookmarked = item in bookmark)
            }
        }

    }

    private val eventsWithoutCategory = repository.getEventsWithoutCategory()
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val eventsWithCategory = selectedCategory.flatMapLatest { category ->
        repository.getEventsWithCategory(category)
    }.cachedIn(viewModelScope)

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val currentPagingFlow: Flow<PagingData<Item>> = selectedCategory
//        .flatMapLatest { category ->
//            if (category == "all") eventsWithoutCategory else eventsWithCategory
//        }
//        .cachedIn(viewModelScope)

    fun selectCategory(category: String) {
        if (category != "all") {
            selectedCategory.value = categoryMap[category]!!
        } else {
            selectedCategory.value = category
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

data class HomeState(
    var events: List<Item> = emptyList(),
    var isLoading: Boolean = false,
    var error: String? = null,
    val nextDataIsLoading: Boolean = false,
    val searchQuery: String = "",
    val category: String = ""
)
