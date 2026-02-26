package com.alfabank.homework.courseproject.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: Long,
    val title: String?,
    val description: String?,
    val bodyText: String?,
    val price: String?,
    val ageRestriction: String?,
    val address: String?,
    val lat: Double?,
    val lon: Double?,
    val placeTitle: String?,
    val startDate: String?,
    val startTime: String?,
    val isEndless: Boolean?,
    val itemUrl: String?,
    val publicationDate: String?,
    val imagesJson: String?, // JSON-строка списка URL
)

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val eventId: Long,
    val prevKey: Int?,
    val nextKey: Int?,
    val category: String?
)

@Entity(
    tableName = "event_category_cross_ref",
    primaryKeys = ["eventId", "category"]
)
data class EventCategoryCrossRef(
    val eventId: Long,
    val category: String
)

data class EventWithCategories(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category",
        associateBy = Junction(
            value = EventCategoryCrossRef::class,
            parentColumn = "eventId",
            entityColumn = "category"
        )
    )
    val categories: List<String>
)