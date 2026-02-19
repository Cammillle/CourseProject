package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import androidx.lifecycle.ViewModel
import com.alfabank.homework.courseproject.data.GuidRepository
import com.alfabank.homework.courseproject.domain.model.Event
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GuidViewModel : ViewModel() {
    private val repository = GuidRepository()

    private val _guidScreenState = MutableStateFlow<HomeState>(HomeState())
    val guidScreenState = _guidScreenState.asStateFlow()


}


data class HomeState(
    var cameraPosition: CameraPosition = CameraPosition(
        Point(59.9342802, 30.3350986),
        11.0f,
        0.0f,
        0.0f
    ),
    var events: List<Event> = emptyList(),
    var selectedEvent: Event? = null,
    var isLoading: Boolean = false,
    var isError: Boolean = false
)