package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "lists_of_event")
@TypeConverters(StringListConverter::class)
data class ListEntity(
    @PrimaryKey
    val id: Long,
    val ctype: String?,
    val title: String?,
    val description: String?,
    val images: List<String>?, // через TypeConverter
    val itemUrl: String?,
    val siteUrl: String?
)