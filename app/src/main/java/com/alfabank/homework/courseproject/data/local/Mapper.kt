package com.alfabank.homework.courseproject.data.local

import com.alfabank.homework.courseproject.domain.Item

fun Item.toItemEntity(): ItemEntity {
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
        isEndless = isEndless
    )
}


fun ItemWithCategories.toItem(): Item {
    return Item(
        id = item.id,
        title = item.title,
        placeTitle = item.placeTitle,
        description = item.description,
        bodyText = item.bodyText,
        ageRestriction = item.ageRestriction,
        address = item.address,
        lat = item.lat,
        lon = item.lon,
        images = item.images,
        price = item.price,
        startDate = item.startDate,
        startTime = item.startTime,
        isEndless = item.isEndless,
        categories = categories.map { it.id },
        itemUrl = item.siteUrl,
        ctype = "event"
    )
}

