package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,          // "concert", "all" и т.д.
    val name: String?,       // отображаемое имя (опционально)
    val nextUrl: String?     // URL следующей страницы для данной категории
)