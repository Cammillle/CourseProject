package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.serialization.json.Json
import kotlin.String

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: Long,
    val title: String?,
    val description: String?,
    val bodyText: String?,
    val price: String?,
    val ageRestriction: String?,
    val address: String?,
    val lat: Double?,
    val lon: Double?,
    val placeTitle: String?,
    val startDate: String?,
    val startTime: String?,
    val isEndless: Boolean?,
    val itemUrl: String?,
    val publicationDate: String?,
    val imagesJson: String?, // JSON-строка списка URL
    val categories: String?, // JSON-строка
)

fun BookmarkEntity.toItem(): Item {
    val images = imagesJson?.let { Json.decodeFromString<List<String>>(it) }
    val categories = categories?.let { Json.decodeFromString<List<String>>(it) }

    return Item(
        id = id,
        title = title,
        startDate = startDate,
        startTime = startTime,
        isEndless = isEndless,
        address = address,
        ageRestriction = ageRestriction,
        description = description,
        bodyText = bodyText,
        images = images,
        price = price,
        itemUrl = itemUrl,
        placeTitle = placeTitle,
        lat = lat,
        lon = lon,
        publicationDate = publicationDate,
        categories = categories
    )
}

fun EventEntity.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        id,
        title,
        description,
        bodyText,
        price,
        ageRestriction,
        address,
        lat,
        lon,
        placeTitle,
        startDate,
        startTime,
        isEndless,
        itemUrl,
        publicationDate,
        imagesJson,
        categories
    )
}