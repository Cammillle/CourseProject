package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val name: String?,
    val nextUrl: String?,
    val lastUpdated: Long? = null
)