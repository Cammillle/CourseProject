package com.alfabank.homework.courseproject.data.local

import android.media.Image
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alfabank.homework.courseproject.domain.model.Coords
import com.alfabank.homework.courseproject.domain.model.DateEvent
import com.alfabank.homework.courseproject.domain.model.EventPlace
import com.alfabank.homework.courseproject.domain.model.ImageEvent
import com.alfabank.homework.courseproject.domain.model.Location

@Entity(tableName = "events_list")
@TypeConverters(StringListConverter::class, DateListConverter::class)
data class EventDBO(
    @PrimaryKey
    val id: Long,
    val title: String?,
    val ageRestriction: String?,
    val price: String?,
    val description: String?,
    val bodyText: String?,
    val siteUrl: String?,

    @Embedded(prefix = "location_") val location: LocationDBO?,
    @Embedded(prefix = "place_") val place: PlaceDBO?,

    val dates: List<DateEvent> = emptyList(),
    val categories: List<String> = emptyList(),

    val mainImageUrl: String? = null

)

data class LocationDBO(
    val lat: Double?,
    val lon: Double?,
    val name: String?,
)

data class PlaceDBO(
    val address: String?,
    val lat: Double?,
    val lon: Double?,
    val title: String?
)

fun PlaceDBO.toEventPlace(): EventPlace {
    return EventPlace(
        address = address,
        coords = Coords(lat = lat, lon = lon),
        id = null,
        isClosed = null,
        isStub = null,
        title = title
    )
}

fun LocationDBO.toLocation(): Location {
    return Location(
        coords = Coords(lat = lat, lon = lon),
        currency = null,
        language = null,
        name = name,
        slug = null,
        timezone = null
    )
}
