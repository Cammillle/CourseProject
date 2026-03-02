package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.GuidRepositoryImpl
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.domain.model.ListItem
import com.alfabank.homework.courseproject.domain.model.ListWithItems
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GuidViewModel : ViewModel() {
    private val repository = GuidRepositoryImpl()
    private val ids =
        listOf<Long>(4058, 14579, 12257, 14475, 14570, 14559, 14560, 7785, 14558, 648, 12384)

    private val _guidScreenState = MutableStateFlow(GuidScreenState())
    val guidScreenState = _guidScreenState.asStateFlow()

    init {
        loadLists()
    }

    fun retry() {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            _guidScreenState.update { state ->
                state.copy(isLoading = true, error = null)
            }
            ids.forEach { id ->
                async {
                    repository.getListsById(id).fold(
                        onSuccess = { it ->
                            _guidScreenState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    lists = state.lists + it
                                )
                            }
                        },
                        onFailure = { error ->
                            _guidScreenState.update { state ->
                                state.copy(
                                    error = error.message ?: "Failed to load more lists",
                                    isLoading = false
                                )
                            }
                        }
                    )
                }
            }

        }
    }


}


data class GuidScreenState(
    var lists: List<ListWithItems> = emptyList(),
    var selectedEvent: Item? = null,
    var isLoading: Boolean = false,
    var error: String? = null
)