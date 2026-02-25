package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity

@Entity(
    tableName = "event_images",
    primaryKeys = ["eventId", "imageUrl"]
)
data class EventImageEntity(
    val eventId: Long,
    val imageUrl: String
)