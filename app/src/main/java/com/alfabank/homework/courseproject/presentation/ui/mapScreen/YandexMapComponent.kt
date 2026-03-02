package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.Item
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Cluster
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.delay

@Suppress("NonSkippableComposable")
@Composable
fun YandexMapComponent(
    modifier: Modifier = Modifier,
    cameraPosition: CameraPosition,
    events: List<Item>,
    selectedEventId: Long?,
    onEventSelected: (Item) -> Unit
) {

    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            val styleJson = """
                [{"elements": ["label"],"stylers": {"visibility": "off"}}]
            """.trimIndent()
            mapWindow.map.setMapStyle(styleJson)
        }
    }

    // Создаём кластеризированную коллекцию ОДИН раз
    val clusterizedCollection = remember(mapView) {
        mapView.mapWindow.map.mapObjects.addClusterizedPlacemarkCollection(
            object : ClusterListener {
                override fun onClusterAdded(cluster: Cluster) {
                    configureClusterAppearance(cluster, context)
                }
            }
        )
    }

    // Установка стартовой камеры (только если позиция реально изменилась)
    LaunchedEffect(cameraPosition) {
        mapView.mapWindow.map.move(
            cameraPosition,
            Animation(Animation.Type.SMOOTH, 0.7f),
            null
        )
    }

    // Обработка нажатий
    DisposableEffect(Unit) {

        val tapListener = MapObjectTapListener { mapObject, _ ->
            if (mapObject is PlacemarkMapObject) {
                val event = mapObject.userData as? Item
                if (event != null) {
                    onEventSelected(event)
                    return@MapObjectTapListener true
                }
            }
            false
        }

        clusterizedCollection.addTapListener(tapListener)

        onDispose {
            clusterizedCollection.removeTapListener(tapListener)
        }
    }


//    // Обновление маркеров
    LaunchedEffect(events, selectedEventId) {
        updateClusterizedMarkers(
            clusterizedCollection = clusterizedCollection,
            events = events,
            selectedEventId = selectedEventId,
            context = context
        )
    }



    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}

/**
 * Функция для настройки внешнего вида кластера.
 */
private fun configureClusterAppearance(
    cluster: Cluster,
    context: Context
) {
    val appearance = cluster.appearance

    appearance.setIcon(
        ImageProvider.fromResource(
            context,
            R.drawable.circle_24_green
        )
    )

    appearance.setIconStyle(
        IconStyle().apply {
            anchor = PointF(0.5f, 0.5f)
            scale = 1.3f
            zIndex = 2000f
        }
    )

    appearance.setText(
        cluster.size.toString(),
        TextStyle().apply {
            size = 14f
            color = Color.WHITE
            placement = TextStyle.Placement.CENTER
            //outlineColor = Color
            //outlineWidth = 1f
        }
    )
}
private fun configurePlacemark(
    placemark: PlacemarkMapObject,
    event: Item,
    isSelected: Boolean,
    context: Context
) {
    updatePlacemarkStyle(placemark, event, isSelected, context)
    placemark.userData = event
}

private fun updatePlacemarkStyle(
    placemark: PlacemarkMapObject,
    event: Item,
    isSelected: Boolean,
    context: Context
) {
    // Иконка
    placemark.setIcon(
        ImageProvider.fromResource(
            context,
            if (isSelected) R.drawable.circle_24_red else R.drawable.circle_24_green
        )
    )
    placemark.setIconStyle(
        IconStyle().apply {
            anchor = PointF(0.5f, 1.0f)
            scale = 1.0f
            zIndex = 1000f
        }
    )
    // Текст
    val title = event.title ?: event.placeTitle ?: ""
    placemark.setText(
        title.replace("+", " "),
        TextStyle().apply {
            size = if (isSelected) 13.0f else 11.0f
            color = if (isSelected) Color.RED else Color.BLACK
            placement = TextStyle.Placement.BOTTOM
            offset = 5.0f
            outlineWidth = if (isSelected) 2.0f else 1.0f
            outlineColor = Color.WHITE
        }
    )
}
/**
 * Обновляет маркеры в кластеризованной коллекции.
 */
private fun updateClusterizedMarkers(
    clusterizedCollection: ClusterizedPlacemarkCollection,
    events: List<Item>,
    selectedEventId: Long?,
    context: Context
) {

    clusterizedCollection.clear()

    val validEvents = events.filter { it.lat != null && it.lon != null }

    val points = validEvents.map { Point(it.lat!!, it.lon!!) }

    val placemarks = clusterizedCollection.addEmptyPlacemarks(points)

    placemarks.forEachIndexed { index, placemark ->

        val event = validEvents[index]

        val isSelected = event.id == selectedEventId

        placemark.setIcon(
            ImageProvider.fromResource(
                context,
                if (isSelected)
                    R.drawable.circle_24_red
                else
                    R.drawable.circle_24_green
            )
        )

        placemark.setIconStyle(
            IconStyle().apply {
                anchor = PointF(0.5f, 1f)
                scale = 1f  // НЕ меняем scale при выборе
                zIndex = if (isSelected) 1500f else 1000f
            }
        )

        placemark.setText(
            (event.title ?: event.placeTitle ?: "").replace("+", " "),
            TextStyle().apply {
                size = 12f
                color = if (isSelected) Color.RED else Color.BLACK
                placement = TextStyle.Placement.BOTTOM
                offset = 5f
                outlineColor = Color.WHITE
                outlineWidth = 1f
            }
        )

        placemark.userData = event
    }

    // ВАЖНО: адекватные параметры
    clusterizedCollection.clusterPlacemarks(
        60.0,  // radius
        12   // minZoom — чтобы подписи появлялись при приближении
    )
}