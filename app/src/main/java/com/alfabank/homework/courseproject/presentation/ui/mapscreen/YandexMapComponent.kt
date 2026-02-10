package com.alfabank.homework.courseproject.presentation.ui.mapscreen

import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.model.Event
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
    events: List<Event> = emptyList(),
    selectedEventId: Long? = null,
    onEventSelected: (Event) -> Unit
) {
    val context = LocalContext.current
    val markersCollection = remember { mutableStateOf<MapObjectCollection?>(null) }
    val tapListenerRef = remember { mutableStateOf<MapObjectTapListener?>(null) }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                val styleJson = """
        [
            {
                "elements": ["label"],
                "stylers": {
                    "visibility": "off"
                }
            }
        ]
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

                mapWindow.map.move(
                    cameraPosition, Animation(Animation.Type.SMOOTH, 1.0f), null
                )
                mapWindow.map.isFastTapEnabled = true
                mapWindow.map.isScrollGesturesEnabled = true
                mapWindow.map.isZoomGesturesEnabled = true
                mapWindow.map.isTiltGesturesEnabled = true
                mapWindow.map.isRotateGesturesEnabled = true

                val collection = mapWindow.map.mapObjects.addCollection()
                markersCollection.value = collection
            }
        },
        update = { mapView ->
            val collection = markersCollection.value ?: return@AndroidView
            collection.clear()

            tapListenerRef.value?.let { oldListener ->
                collection.removeTapListener(oldListener)
            }

            events.forEachIndexed { index, event ->
                val place = event.place
                val placeCoords = place?.coords
                val location = event.location
                val locationCoords = location?.coords
                val coords = placeCoords ?: locationCoords

                coords.let { coords ->
                    val point = Point(coords?.lat!!, coords.lon!!)

                    collection.addPlacemark().apply {
                        geometry = point
                        setIcon(
                            ImageProvider.fromResource(
                                context, if (event.id == selectedEventId) {
                                    R.drawable.circle_24_red
                                } else {
                                    R.drawable.circle_24_green
                                }
                            )
                        )

                        // Настраиваем стиль маркера
                        setIconStyle(
                            IconStyle().apply {
                                anchor = PointF(0.5f, 1.0f)
                                flat = false
                                scale = 1.0f
                                zIndex = 1f
                            })

                        userData = event

                        // Добавляем текст под маркером
                        event.place?.title?.let { title ->
                            setText(
                                title, TextStyle().apply {
                                    size = if (event.id == selectedEventId) 13.0f else 11.0f
                                    color =
                                        if (event.id == selectedEventId) Color.RED else Color.BLACK
                                    placement = TextStyle.Placement.BOTTOM
                                    offset = 5.0f
                                    outlineWidth =
                                        if (event.id == selectedEventId) 2.0f else 1.0f
                                    outlineColor = Color.WHITE
                                })
                        }


                    }
                }
            }

            val newTapListener = MapObjectTapListener { mapObject, point ->
                if (mapObject is PlacemarkMapObject) {
                    val tappedEvent = mapObject.userData as? Event
                    tappedEvent?.let {
                        Log.d("TAGTAG", "Клик по маркеру события: ${it.id}")
                        onEventSelected(it)

                        mapView.mapWindow.map.move(
                            CameraPosition(
                                point,
                                mapView.mapWindow.map.cameraPosition.zoom.coerceAtLeast(15f),
                                mapView.mapWindow.map.cameraPosition.azimuth,
                                mapView.mapWindow.map.cameraPosition.tilt
                            ), Animation(Animation.Type.SMOOTH, 0.3f), null
                        )
                        return@MapObjectTapListener true
                    }
                }
                false
            }

            tapListenerRef.value = newTapListener

            collection.addTapListener(newTapListener)
        },
        modifier = modifier
    )

}