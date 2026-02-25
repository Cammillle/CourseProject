package com.alfabank.homework.courseproject.data.local

import com.alfabank.homework.courseproject.data.dto.EventDTO
import com.alfabank.homework.courseproject.domain.Item

fun EventDTO.toEventEntity() = EventEntity(
    id = id,
    title = title,
    description = description,
    bodyText = bodyText,
    price = price,
    ageRestriction = ageRestriction,
    address = place?.address,
    lat = place?.coords?.lat ?: location?.coords?.lat,
    lon = place?.coords?.lon ?: location?.coords?.lon,
    placeTitle = place?.title,
    startDate = dates?.last()?.startDate,
    startTime = dates?.last()?.startTime,
    isEndless = dates?.last()?.isEndless,
    itemUrl = siteUrl,
    cachedAt = System.currentTimeMillis(),
    publicationDate = publicationDate ?: ""
)

fun EventDTO.toCategoryRefs() =
    categories?.map {
        EventCategoryCrossRef(id, it)
    } ?: emptyList()

fun EventDTO.toImageEntities() =
    images?.mapNotNull {
        it.thumbnails?.x384?.let { url ->
            EventImageEntity(id, url)
        }
    } ?: emptyList()


fun EventDTO.toDatasetRef(datasetKey: String) =
    DatasetEventCrossRef(id, datasetKey)

fun EventWithRelations.toDomain() = Item(
    id = event.id,
    startDate = event.startDate,
    startTime = event.startTime,
    isEndless = event.isEndless,
    address = event.address,
    ageRestriction = event.ageRestriction,
    description = event.description,
    bodyText = event.bodyText,
    images = images,
    categories = categories,
    price = event.price,
    itemUrl = event.itemUrl,
    title = event.title,
    placeTitle = event.placeTitle,
    lat = event.lat,
    lon = event.lon
)