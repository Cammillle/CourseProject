package com.alfabank.homework.courseproject.data.local

import com.alfabank.homework.courseproject.domain.Item

fun Item.toItemEntity(category: String): ItemEntity {
    return ItemEntity(
        id = id,
        listId = null,
        title = title,
        placeTitle = placeTitle,
        description = description,
        bodyText = bodyText,
        ageRestriction = ageRestriction,
        address = address,
        lat = lat,
        lon = lon,
        images = images?.map { it.toString() },
        price = price,
        siteUrl = itemUrl,
        startDate = startDate,
        startTime = startTime,
        isEndless = isEndless,
        category = category
    )
}


fun ItemEntity.toItem(): Item {
    return Item(
        id = id,
        title = title,
        placeTitle = placeTitle,
        description = description,
        bodyText = bodyText,
        ageRestriction = ageRestriction,
        address = address,
        lat = lat,
        lon = lon,
        images = images,
        price = price,
        startDate = startDate,
        startTime = startTime,
        ctype = "event",
        isEndless = isEndless,
        categories = listOf(category.toString()),
        itemUrl = siteUrl,
    )
}

