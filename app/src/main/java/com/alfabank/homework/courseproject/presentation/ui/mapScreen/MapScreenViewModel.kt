package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import androidx.lifecycle.ViewModel
import com.alfabank.homework.courseproject.domain.Item
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MapScreenViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(MapScreenState())
    val screenState = _screenState.asStateFlow()

    fun selectEvent(event: Item) {
        _screenState.update { currentState ->
            currentState.copy(
                selectedEvent = event,
                cameraPosition = CameraPosition(
                    Point(
                        event.lat ?: 59.939094,
                        event.lon ?: 30.315868
                    ),
                    15f,
                    currentState.cameraPosition.azimuth,
                    currentState.cameraPosition.tilt
                )
            )
        }
    }
    fun clearSelection() {
        _screenState.update { it.copy(selectedEvent = null) }
    }
}

data class MapScreenState(
    var cameraPosition: CameraPosition = CameraPosition(
        Point(59.9342802, 30.3350986),
        11.0f,
        0.0f,
        0.0f
    ),
    var events: List<Item> = emptyList(),
    var selectedEvent: Item? = null,
    var isLoading: Boolean = false,
    var isError: Boolean = false
)