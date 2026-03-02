package com.alfabank.homework.courseproject.data.local

import android.util.Log
import com.alfabank.homework.courseproject.data.dto.event.EventDTO
import com.alfabank.homework.courseproject.data.local.dbo.EventEntity
import com.alfabank.homework.courseproject.domain.model.Item
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun EventDTO.toEventEntity(
    isFavourite: Boolean?
): EventEntity {

    Log.d("TAGTAG", "toEventEntity ${this.place?.siteUrl}")
    val firstDate = dates?.last()
    val startDate = firstDate?.startDate
    val startTime = firstDate?.startTime
    val isEndless = firstDate?.isEndless

    val imagesJson = images?.mapNotNull { it.image }?.let { Json.encodeToString(it) }
    val categoriesJson = categories?.let { Json.encodeToString(it) }
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
        itemUrl = siteUrl ?: place?.siteUrl,
        placeTitle = place?.title,
        lat = place?.coords?.lat ?: location?.coords?.lat,
        lon = place?.coords?.lon ?: location?.coords?.lon,
        publicationDate = publicationDate,
        categories = categoriesJson,
        city = location?.slug ?: "",
        isFavourite = isFavourite ?: false
    )
}

fun EventEntity.toItem(): Item {
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
        categories = categories,
        isFavourite = isFavourite,
        city = city
    )
}
