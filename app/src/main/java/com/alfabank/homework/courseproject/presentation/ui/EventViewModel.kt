package com.alfabank.homework.courseproject.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.data.local.EventsRemoteMediator
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
import java.lang.reflect.Array.set

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

    val selectedCategory = MutableStateFlow<String?>(null)

    val events = selectedCategory
        .flatMapLatest { category ->
            repository.getEvents(category)
        }
        .cachedIn(viewModelScope)

    fun selectCategory(category: String?) {
        selectedCategory.value = categoryMap[category]
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
