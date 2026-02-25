package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paging_keys")
data class PagingKeys(
    @PrimaryKey val datasetKey: String,
    val nextKey: Int?,
    val lastUpdated: Long
)