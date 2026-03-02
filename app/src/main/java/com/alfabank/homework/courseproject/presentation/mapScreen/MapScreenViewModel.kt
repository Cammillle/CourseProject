package com.alfabank.homework.courseproject.presentation.mapScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.domain.model.Item
import com.alfabank.homework.courseproject.domain.model.ListItem
import com.alfabank.homework.courseproject.navigation.MapScreenArgs
import com.alfabank.homework.courseproject.navigation.Screen
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: MapScreenArgs? =
        savedStateHandle
            .get<String>(Screen.KEY_MAP_ARGS)
            ?.let { json ->
                Json.decodeFromString<MapScreenArgs>(json)
            }

    private val _screenState = MutableStateFlow(createInitialState(args))
    val screenState = _screenState.asStateFlow()

    private fun createInitialState(args: MapScreenArgs?): MapScreenState {
        return when (args) {
            is MapScreenArgs.ListData -> {
                val listItem = args.listItem.listItem
                val items = args.listItem.items ?: emptyList()
                Log.d("MapScreen", "items of listItem $items")
                MapScreenState(
                    cameraPosition = calculateCameraPositionForItems(items),
                    events = items,
                    listItem = listItem
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
                MapScreenState(
                    cameraPosition = CameraPosition(Point(59.9342802, 30.3350986), 11f, 0f, 0f),
                    events = emptyList()
                )
            }
        }
    }

    private val DEFAULT_POINT = Point(59.9342802, 30.3350986)

    private fun calculateCameraPositionForItems(items: List<Item>): CameraPosition {
        val validPoints = items
            .mapNotNull { item ->
                val lat = item.lat
                val lon = item.lon
                if (lat != null && lon != null) Point(lat, lon) else null
            }

        if (validPoints.isEmpty()) {
            return CameraPosition(
                DEFAULT_POINT,
                11f,
                0f,
                0f
            )
        }

        // Средняя точка
        val avgLat = validPoints.map { it.latitude }.average()
        val avgLon = validPoints.map { it.longitude }.average()

        // разброс точек
        val minLat = validPoints.minOf { it.latitude }
        val maxLat = validPoints.maxOf { it.latitude }
        val minLon = validPoints.minOf { it.longitude }
        val maxLon = validPoints.maxOf { it.longitude }

        val latDelta = maxLat - minLat
        val lonDelta = maxLon - minLon
        val maxDelta = maxOf(latDelta, lonDelta)

        //для зума
        val zoom = when {
            maxDelta < 0.01 -> 15f
            maxDelta < 0.05 -> 13f
            maxDelta < 0.2  -> 11f
            maxDelta < 1.0  -> 9f
            else            -> 7f
        }

        return CameraPosition(
            Point(avgLat, avgLon),
            zoom,
            0f,
            0f
        )
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
    val listItem: ListItem? = null,
    val events: List<Item> = emptyList(),
    val selectedEvent: Item? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)