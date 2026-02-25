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
    val cachedAt: Long,
    val publicationDate: String
)

