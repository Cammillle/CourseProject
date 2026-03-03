package com.alfabank.homework.courseproject.data.local.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: Long,
    val title: String?,
    val city: String,
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
    val imagesJson: String?,
    val categories: String?,
    val isFavourite: Boolean = false
)

@Entity(
    tableName = "remote_keys",
    primaryKeys = ["eventId", "category", "city"]
)
data class RemoteKeys(
    val eventId: Long,
    val prevKey: Int?,
    val nextKey: Int?,
    val category: String,
    val city: String
)

@Entity(
    tableName = "event_category_cross_ref",
    primaryKeys = ["eventId", "category"]
)
data class EventCategoryCrossRef(
    val eventId: Long,
    val category: String,
)