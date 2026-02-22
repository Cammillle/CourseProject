package com.alfabank.homework.courseproject.data

import com.alfabank.homework.courseproject.data.dto.EventDTO
import com.alfabank.homework.courseproject.data.dto.ListOfEventsResponseDTO
import com.alfabank.homework.courseproject.domain.Item


fun ListOfEventsResponseDTO.toListEvent(): List<Item>? {
    return results?.map { it.toItem() }
}

fun EventDTO.toItem(): Item {
    return Item(
        id = id,
        ctype = "event",
        startDate = dates?.get(dates.size - 1)?.startDate,
        startTime = dates?.get(dates.size - 1)?.startTime,
        address = place?.address,
        ageRestriction = ageRestriction,
        description = description,
        images = images?.map { it.thumbnails?.x384 },
        categories = categories,
        price = price,
        itemUrl = siteUrl,
        title = title,
        placeTitle = place?.title,
        lat = place?.coords?.lat ?: location?.coords?.lat,
        lon = place?.coords?.lon ?: location?.coords?.lon,
        isEndless = dates?.get(dates.size - 1)?.isEndless,
        bodyText = bodyText,
    )
}

