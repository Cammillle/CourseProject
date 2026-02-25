package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity

@Entity(
    tableName = "dataset_event_cross_ref",
    primaryKeys = ["eventId", "datasetKey"]
)
data class DatasetEventCrossRef(
    val eventId: Long,
    val datasetKey: String
)