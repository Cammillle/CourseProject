package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.data.dto.lists.Item
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun YandexMapComponent(
    modifier: Modifier = Modifier,
    cameraPosition: CameraPosition,
    events: List<Item> = emptyList(),
    selectedEventId: Long? = null,
    onEventSelected: (Item) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    LaunchedEffect(events, selectedEventId) {
        if (mapView.mapWindow.map.isValid()) {
            updateMarkers(mapView, events, selectedEventId, context, onEventSelected)
        }


    }

    LaunchedEffect(cameraPosition) {
        if (mapView.mapWindow.map.isValid) {
            mapView.mapWindow.map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, 1.0f),
                null
            )
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}

private fun updateMarkers(
    mapView: MapView,
    events: List<Item>,
    selectedEventId: Long?,
    context: Context,
    onEventSelected: (Item) -> Unit
) {
    Log.d("MapScreen", "Update Markers. Events count: ${events.size}")

    mapView.mapWindow.map.mapObjects.clear()
    val collection = mapView.mapWindow.map.mapObjects.addCollection()

    var markersAdded = 0

    events.forEach { event ->
        Log.d("MapScreen", "Event id: ${event.id}, place: ${event.place}, coords: ${event.coords}")

        val coords = event.place?.coords ?: event.coords
        if (coords != null && coords.lat != null && coords.lon != null) {
            try {
                val point = Point(coords.lat, coords.lon)
                Log.d("MapScreen", "Adding marker at $point")

                val placemark = collection.addPlacemark().apply {
                    geometry = point
                    setIcon(
                        ImageProvider.fromResource(
                            context,
                            if (event.id == selectedEventId) R.drawable.circle_24_red
                            else R.drawable.circle_24_green
                        )
                    )
                    setIconStyle(
                        IconStyle().apply {
                            anchor = PointF(0.5f, 1.0f)
                            flat = false
                            scale = 1.0f
                            zIndex = 1000f
                        }
                    )
                    userData = event
                    event.title?.let {
                        setText(
                            event.title.replace("+"," "),
                            TextStyle().apply {
                                size = if (event.id == selectedEventId) 13.0f else 11.0f
                                color = if (event.id == selectedEventId) Color.RED else Color.BLACK
                                placement = TextStyle.Placement.BOTTOM
                                offset = 5.0f
                                outlineWidth = if (event.id == selectedEventId) 2.0f else 1.0f
                                outlineColor = Color.WHITE
                            }
                        )
                    } ?: event.place?.title?.let { title ->
                        setText(
                            title,
                            TextStyle().apply {
                                size = if (event.id == selectedEventId) 13.0f else 11.0f
                                color = if (event.id == selectedEventId) Color.RED else Color.BLACK
                                placement = TextStyle.Placement.BOTTOM
                                offset = 5.0f
                                outlineWidth = if (event.id == selectedEventId) 2.0f else 1.0f
                                outlineColor = Color.WHITE
                            }
                        )
                    }
                }
                markersAdded++
                Log.d("MapScreen", "Placemark added: $placemark")
            } catch (e: Exception) {
                Log.e("MapScreen", "Error adding marker for event ${event.id}", e)
            }
        } else {
            Log.d("MapScreen", "Skipping event ${event.id}: no valid coordinates")
        }
    }

    Log.d("MapScreen", "Total markers added: $markersAdded")

    val tapListener = MapObjectTapListener { mapObject, point ->
        Log.d("MapScreen","TAP TAP")
        if (mapObject is PlacemarkMapObject) {
            val tappedEvent = mapObject.userData as? Item
            tappedEvent?.let {
                onEventSelected(it)
                return@MapObjectTapListener true
            }
        }
        false
    }
    collection.addTapListener(tapListener)
}