package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

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
    val categoriesJson: String?, // JSON-строка списка категорий
    val queryId: String // идентификатор текущего фильтра
)

@Entity(tableName = "paging_metadata")
data class PagingMetadataEntity(
    @PrimaryKey
    val queryId: String,
    val nextUrl: String?,
    val isEndReached: Boolean
)

