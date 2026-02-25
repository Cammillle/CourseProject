package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity

@Entity(
    tableName = "remote_keys",
    primaryKeys = ["eventId", "datasetKey"]
)
data class RemoteKeys(
    val eventId: Long,
    val datasetKey: String,
    val prevKey: Int?,
    val nextKey: Int?
)