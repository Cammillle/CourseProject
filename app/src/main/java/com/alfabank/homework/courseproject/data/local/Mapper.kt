package com.alfabank.homework.courseproject.data.local

import com.alfabank.homework.courseproject.data.dto.EventDTO
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun EventDTO.toEventEntity(dto: EventDTO, queryId: String): EventEntity {
    val firstDate = dto.dates?.last()
    val startDate = firstDate?.startDate
    val startTime = firstDate?.startTime
    val isEndless = firstDate?.isEndless

    val imagesJson = dto.images?.mapNotNull { it.image }?.let { Json.encodeToString(it) }
    val categoriesJson = dto.categories?.let { Json.encodeToString(it) }
    return EventEntity(
        id = dto.id,
        title = dto.title,
        startDate = startDate,
        startTime = startTime,
        isEndless = isEndless,
        address = dto.place?.address,
        ageRestriction = dto.ageRestriction,
        description = dto.description,
        bodyText = dto.bodyText,
        imagesJson = imagesJson,
        categoriesJson = categoriesJson,
        price = dto.price,
        itemUrl = dto.siteUrl,
        placeTitle = dto.place?.title,
        lat = dto.place?.coords?.lat ?: dto.location?.coords?.lat,
        lon = dto.place?.coords?.lon ?: dto.location?.coords?.lon,
        publicationDate = dto.publicationDate,
        queryId = queryId
    )
}

fun EventEntity.toItem(): Item {
    val images = imagesJson?.let { Json.decodeFromString<List<String>>(it) }
    val categories = categoriesJson?.let { Json.decodeFromString<List<String>>(it) }

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
        categories = categories,
        price = price,
        itemUrl = itemUrl,
        placeTitle = placeTitle,
        lat = lat,
        lon = lon,
        publicationDate = publicationDate
    )
}
