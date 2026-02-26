package com.alfabank.homework.courseproject.data.local

import com.alfabank.homework.courseproject.data.dto.EventDTO
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun EventDTO.toEventEntity(): EventEntity {
    val firstDate = dates?.last()
    val startDate = firstDate?.startDate
    val startTime = firstDate?.startTime
    val isEndless = firstDate?.isEndless

    val imagesJson = images?.mapNotNull { it.image }?.let { Json.encodeToString(it) }
    return EventEntity(
        id = id,
        title = title,
        startDate = startDate,
        startTime = startTime,
        isEndless = isEndless,
        address = place?.address,
        ageRestriction = ageRestriction,
        description = description,
        bodyText = bodyText,
        imagesJson = imagesJson,
        price = price,
        itemUrl = siteUrl,
        placeTitle = place?.title,
        lat = place?.coords?.lat ?: location?.coords?.lat,
        lon = place?.coords?.lon ?: location?.coords?.lon,
        publicationDate = publicationDate,
    )
}

fun EventEntity.toItem(): Item {
    val images = imagesJson?.let { Json.decodeFromString<List<String>>(it) }

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
        categories = listOf("")
    )
}
