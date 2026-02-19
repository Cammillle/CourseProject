package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.GuidRepositoryImpl
import com.alfabank.homework.courseproject.data.dto.lists.ListItemResponse
import com.alfabank.homework.courseproject.domain.model.Event
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GuidViewModel : ViewModel() {
    private val repository = GuidRepositoryImpl()
    private val ids = listOf(4058, 14579)


    private val _guidScreenState = MutableStateFlow<GuidScreenState>(GuidScreenState())
    val guidScreenState = _guidScreenState.asStateFlow()

    init {
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
                                    error = error.message ?: "Failed to load more lists"
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
    var cameraPosition: CameraPosition = CameraPosition(
        Point(59.9342802, 30.3350986),
        11.0f,
        0.0f,
        0.0f
    ),
    var lists: List<ListItemResponse> = emptyList(),
    var selectedEvent: Event? = null,
    var isLoading: Boolean = false,
    var error: String? = null
)