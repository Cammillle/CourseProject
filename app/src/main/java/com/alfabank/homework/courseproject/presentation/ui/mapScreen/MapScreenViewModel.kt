package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.dto.lists.Item
import com.alfabank.homework.courseproject.navigation.Screen
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapScreenViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(MapScreenState())
    val screenState = _screenState.asStateFlow()

    fun selectEvent(event: Item) {
        _screenState.update { currentState ->
            val coords = event.place?.coords ?: event.coords
            currentState.copy(
                selectedEvent = event,
                cameraPosition = CameraPosition(
                    Point(
                        coords?.lat ?: 59.939094,
                        coords?.lon?: 30.315868
                    ),
                    15f,
                    currentState.cameraPosition.azimuth,
                    currentState.cameraPosition.tilt
                )
            )
        }
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