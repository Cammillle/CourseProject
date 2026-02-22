package com.alfabank.homework.courseproject.data.local

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        return value?.split(",")?.map { it.trim() } ?: emptyList()
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
}
