package com.alfabank.homework.courseproject.data

import com.alfabank.homework.courseproject.data.dto.CoordsDTO
import com.alfabank.homework.courseproject.data.dto.DateDTO
import com.alfabank.homework.courseproject.data.dto.EventPlaceDTO
import com.alfabank.homework.courseproject.data.dto.EventDTO
import com.alfabank.homework.courseproject.data.dto.ImageDTO
import com.alfabank.homework.courseproject.data.dto.ListOfEventsResponseDTO
import com.alfabank.homework.courseproject.data.dto.LocationDTO
import com.alfabank.homework.courseproject.data.dto.ThumbnailsDTO
import com.alfabank.homework.courseproject.data.places.PlaceDTO
import com.alfabank.homework.courseproject.domain.model.Coords
import com.alfabank.homework.courseproject.domain.model.DateEvent
import com.alfabank.homework.courseproject.domain.model.Event
import com.alfabank.homework.courseproject.domain.model.ImageEvent
import com.alfabank.homework.courseproject.domain.model.Location
import com.alfabank.homework.courseproject.domain.model.EventPlace
import com.alfabank.homework.courseproject.domain.model.Thumbnails
import com.alfabank.homework.courseproject.domain.places.Place


fun ListOfEventsResponseDTO.toListEvent(): List<Event>? {
    return results?.map { it.toEvent() }
}

fun PlaceDTO.toPlace(): Place {
    return Place(
        address = address ?: "",
        categories = categories ?: emptyList(),
        coords = coords?.toCoords(),
        description = description ?: "",
        id = id ?: 0,
        images = images?.map { it.toImageEvent() },
        tags = tags ?: emptyList(),
        title = title ?: "",
        isFree = isFree ?: true,
        timetable = timetable ?: ""
    )
}

fun EventDTO.toEvent(): Event {
    return Event(
        dates = dates?.map { it.toDateEvent() },
        id = id,
        place = place?.toPlace(),
        title = title,
        description = description,
        images = images?.map { it.toImageEvent() },
        categories = categories,
        ageRestriction = ageRestriction,
        price = price,
        location = location?.toLocation(),
        bodyText = bodyText ?: "",
        siteUrl = siteUrl ?: ""
    )
}

fun EventPlaceDTO.toPlace(): EventPlace {
    return EventPlace(
        address = address,
        coords = coords?.toCoords(),
        id = id,
        isClosed = isClosed,
        isStub = isStub,
        title = title
    )
}

fun DateDTO.toDateEvent(): DateEvent {
    return DateEvent(
        endDate = endDate,
        endTime = endTime,
        isContinuous = isContinuous,
        isEndless = isEndless,
        isStartless = isStartless,
        startDate = startDate,
        startTime = startTime,
        usePlaceSchedule = usePlaceSchedule
    )
}

fun CoordsDTO.toCoords(): Coords {
    return Coords(
        lat = lat ?: 0.0,
        lon = lon ?: 0.0
    )
}

fun ImageDTO.toImageEvent(): ImageEvent {
    return ImageEvent(
        image = image,
        thumbnails = thumbnails?.toThumbnails()
    )
}

fun ThumbnailsDTO.toThumbnails(): Thumbnails {
    return Thumbnails(
        x384 = x384,
        x96 = x96
    )
}

fun LocationDTO.toLocation(): Location {
    return Location(
        coords = coords?.toCoords(),
        currency = currency,
        language = language,
        name = name,
        slug = slug,
        timezone = timezone
    )
}