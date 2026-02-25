package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity

@Entity(
    tableName = "event_category_cross_ref",
    primaryKeys = ["eventId", "category"]
)
data class EventCategoryCrossRef(
    val eventId: Long,
    val category: String
)