package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alfabank.homework.courseproject.data.dto.lists.ItemDTO
import com.alfabank.homework.courseproject.domain.Item

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: Long,

    val address: String?,
    val ageRestriction: String?,
    val description: String?,
    val bodyText: String?,

    val image: String,
    val firstImage: String?,
    val itemUrl: String?,
    val title: String?,
    val placeTitle: String?,
    val lat: Double?,
    val lon: Double?
)

fun ItemDTO.toItemEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        address = address,
        ageRestriction = ageRestriction,
        description = description,
        bodyText = bodyText,
        image = firstImage?.thumbnails?.x384 ?: "",
        itemUrl = itemUrl,
        title = title,
        placeTitle = place?.title ?: "",
        lat = place?.coords?.lat ?: coords?.lat,
        lon = place?.coords?.lon ?: coords?.lon,
        firstImage = firstImage?.thumbnails?.x384
    )
}

fun ItemEntity.toItem(): Item {
    return Item(
        id = id,
        title = title,
        address = address,
        ageRestriction = ageRestriction,
        description = description,
        bodyText = bodyText,
        itemUrl = itemUrl,
        placeTitle = placeTitle,
        lat = lat,
        lon = lon,
        startDate = "",
        startTime = "",
        isEndless = false,
        city = "",
        images = listOf(firstImage),
        categories = emptyList(),
        price = "",
        isFavourite = false,
        publicationDate = null
    )
}