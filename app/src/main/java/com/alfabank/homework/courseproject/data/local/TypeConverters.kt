package com.alfabank.homework.courseproject.data.local

import androidx.room.TypeConverter
import com.alfabank.homework.courseproject.domain.model.DateEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

class DateListConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @TypeConverter
    fun fromString(value: String?): List<DateEvent>? {
        return if (value.isNullOrBlank()) {
            null
        } else {
            try {
                json.decodeFromString(value)
            } catch (e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toString(dateList: List<DateEvent>?): String? {
        return if (dateList.isNullOrEmpty()) {
            null
        } else {
            try {
                json.encodeToString(dateList)
            } catch (e: Exception) {
                null
            }
        }
    }
}