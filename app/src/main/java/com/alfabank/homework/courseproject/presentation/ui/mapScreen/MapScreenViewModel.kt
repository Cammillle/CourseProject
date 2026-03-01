package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.navigation.MapScreenArgs
import com.alfabank.homework.courseproject.navigation.Screen
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapScreenViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: MapScreenArgs? = savedStateHandle.get<MapScreenArgs>(Screen.KEY_MAP_ARGS)

    private val _screenState = MutableStateFlow(createInitialState(args))
    val screenState = _screenState.asStateFlow()


    private fun createInitialState(args: MapScreenArgs?): MapScreenState {
        return when (args) {
            is MapScreenArgs.ListData -> {
                val items = args.listItem.items ?: emptyList()
                MapScreenState(
                    cameraPosition = calculateCameraPositionForItems(items),
                    events = items,
                )
            }

            is MapScreenArgs.SingleItem -> {
                val item = args.item
                MapScreenState(
                    cameraPosition = CameraPosition(
                        Point(item.lat ?: 59.939094, item.lon ?: 30.315868),
                        15f,
                        0f,
                        0f
                    ),
                    events = listOf(item),
                )
            }

            null -> {
                // Значения по умолчанию, если аргументов нет (маловероятно)
                MapScreenState(
                    cameraPosition = CameraPosition(Point(59.9342802, 30.3350986), 11f, 0f, 0f),
                    events = emptyList()
                )
            }
        }
    }

    private fun calculateCameraPositionForItems(items: List<Item>): CameraPosition {
        // Реализация: найти среднюю точку или использовать первую точку с подходящим зумом
        return if (items.isNotEmpty()) {
            CameraPosition(
                Point(items.first().lat ?: 59.939094, items.first().lon ?: 30.315868),
                11f,
                0f,
                0f
            )
        } else {
            CameraPosition(Point(59.9342802, 30.3350986), 11f, 0f, 0f)
        }
    }

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

    fun updateArgs(newArgs: MapScreenArgs) {
        viewModelScope.launch {
            _screenState.update { createInitialState(newArgs) }
        }
    }
}

data class MapScreenState(
    val cameraPosition: CameraPosition = CameraPosition(
        Point(59.9342802, 30.3350986),
        11.0f,
        0.0f,
        0.0f
    ),
    val events: List<Item> = emptyList(),
    val selectedEvent: Item? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)