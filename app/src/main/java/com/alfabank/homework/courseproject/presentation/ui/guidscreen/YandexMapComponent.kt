package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.model.Event
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView

@Composable
fun YandexMapComponent(
    modifier: Modifier = Modifier,
    cameraPosition: CameraPosition,
    events: List<Event> = emptyList(),
    selectedEventId: Long? = null,
    onEventSelected: (Event) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val markersCollection = remember { mutableStateOf<MapObjectCollection?>(null) }
    val tapListenerRef = remember { mutableStateOf<MapObjectTapListener?>(null) }

    val mapView = remember {
        MapView(context).apply {
            val styleJson = """
                [{"elements": ["label"],"stylers": {"visibility": "off"}}]
            """.trimIndent()
            mapWindow.map.setMapStyle(styleJson)
            try {
                val mapKit = MapKitFactory.getInstance()
                val userLocationLayer = mapKit.createUserLocationLayer(mapWindow)
                userLocationLayer.isVisible = false
                userLocationLayer.isHeadingModeActive = false
            } catch (e: SecurityException) {
                Log.d("MapKit", "Location disabled: ${e.message}")
            }
            mapWindow.map.isFastTapEnabled = true
            mapWindow.map.isScrollGesturesEnabled = true
            mapWindow.map.isZoomGesturesEnabled = true
            mapWindow.map.isTiltGesturesEnabled = true
            mapWindow.map.isRotateGesturesEnabled = true
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onStop()
        }
    }

    // Обновляем маркеры при изменении данных
    LaunchedEffect(events, selectedEventId) {
        // Проверяем, что карта жива
        if (mapView.mapWindow.map.isValid()) {
            updateMarkers(mapView, events, selectedEventId, context, onEventSelected)
        }
    }

    // Обновляем позицию камеры при её изменении
    LaunchedEffect(cameraPosition) {
        if (mapView.mapWindow.map.isValid()) {
            mapView.mapWindow.map.move(
                cameraPosition,
                com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 1.0f),
                null
            )
        }
    }

    AndroidView(
        factory = {mapView},
        modifier = modifier
    )
}

private fun updateMarkers(
    mapView: MapView,
    events: List<Event>,
    selectedEventId: Long?,
    context: android.content.Context,
    onEventSelected: (Event) -> Unit
) {
    val collection = mapView.mapWindow.map.mapObjects.addCollection()
    collection.clear()

    events.forEach { event ->
        val coords = event.place?.coords ?: event.location?.coords
        coords?.let {
            val point = com.yandex.mapkit.geometry.Point(it.lat!!, it.lon!!)
            val placemark = collection.addPlacemark().apply {
                geometry = point
                setIcon(
                    com.yandex.runtime.image.ImageProvider.fromResource(
                        context,
                        if (event.id == selectedEventId) R.drawable.circle_24_red
                        else R.drawable.circle_24_green
                    )
                )
                setIconStyle(
                    com.yandex.mapkit.map.IconStyle().apply {
                        anchor = android.graphics.PointF(0.5f, 1.0f)
                        flat = false
                        scale = 1.0f
                        zIndex = 1f
                    }
                )
                userData = event
                event.place?.title?.let { title ->
                    setText(
                        title,
                        com.yandex.mapkit.map.TextStyle().apply {
                            size = if (event.id == selectedEventId) 13.0f else 11.0f
                            color = if (event.id == selectedEventId) android.graphics.Color.RED
                            else android.graphics.Color.BLACK
                            placement = com.yandex.mapkit.map.TextStyle.Placement.BOTTOM
                            offset = 5.0f
                            outlineWidth = if (event.id == selectedEventId) 2.0f else 1.0f
                            outlineColor = android.graphics.Color.WHITE
                        }
                    )
                }
            }
        }
    }

    // Обработчик кликов
    val tapListener = com.yandex.mapkit.map.MapObjectTapListener { mapObject, point ->
        if (mapObject is com.yandex.mapkit.map.PlacemarkMapObject) {
            val tappedEvent = mapObject.userData as? Event
            tappedEvent?.let {
                onEventSelected(it)
                mapView.mapWindow.map.move(
                    CameraPosition(
                        point,
                        mapView.mapWindow.map.cameraPosition.zoom.coerceAtLeast(15f),
                        mapView.mapWindow.map.cameraPosition.azimuth,
                        mapView.mapWindow.map.cameraPosition.tilt
                    ),
                    com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 0.3f),
                    null
                )
                return@MapObjectTapListener true
            }
        }
        false
    }
    collection.addTapListener(tapListener)
}