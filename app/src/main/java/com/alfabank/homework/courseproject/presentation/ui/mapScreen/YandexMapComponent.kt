package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.Item
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
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

    val mapObjectCollection = remember {
        mapView.mapWindow.map.mapObjects.addCollection()
    }

    DisposableEffect(Unit) {
        val tapListener = MapObjectTapListener { mapObject, _ ->
            if (mapObject is PlacemarkMapObject) {
                val tappedEvent = mapObject.userData as? Item
                tappedEvent?.let {
                    onEventSelected(it)
                    return@MapObjectTapListener true
                }
            }
            false
        }

        mapObjectCollection.addTapListener(tapListener)

        onDispose {
            mapObjectCollection.removeTapListener(tapListener)
        }
    }

    LaunchedEffect(events, selectedEventId) {
        if (mapView.mapWindow.map.isValid) {
            updateMarkers(
                mapObjectCollection,
                events,
                selectedEventId,
                context
            )
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
    collection: MapObjectCollection,
    events: List<Item>,
    selectedEventId: Long?,
    context: Context
) {
    collection.clear()

    events.forEach { event ->
        if (event.lat != null && event.lon != null) {

            val placemark = collection.addPlacemark(
                Point(event.lat,event.lon)
            )

            placemark.setIcon(
                ImageProvider.fromResource(
                    context,
                    if (event.id == selectedEventId)
                        R.drawable.circle_24_red
                    else
                        R.drawable.circle_24_green
                )
            )

            placemark.setIconStyle(
                IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f)
                    flat = false
                    scale = 1.0f
                    zIndex = 1000f
                }
            )
            val title = event.placeTitle ?: event.title ?: ""
            placemark.setText(
                title.replace("+", " "),
                TextStyle().apply {
                    size = if (event.id == selectedEventId) 13.0f else 11.0f
                    color = if (event.id == selectedEventId) Color.RED else Color.BLACK
                    placement = TextStyle.Placement.BOTTOM
                    offset = 5.0f
                    outlineWidth = if (event.id == selectedEventId) 2.0f else 1.0f
                    outlineColor = Color.WHITE
                }
            )

            placemark.userData = event
        }
    }
}