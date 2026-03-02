package com.alfabank.homework.courseproject.data.dto.lists

import com.alfabank.homework.courseproject.data.dto.event.CoordsDTO
import com.alfabank.homework.courseproject.data.dto.event.EventPlaceDTO
import com.alfabank.homework.courseproject.domain.model.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull

@Serializable
data class ItemDTO(
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("ctype")
    val ctype: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("age_restriction")
    val ageRestrictionRaw: JsonElement?,
    @SerialName("description")
    val description: String?,
    @SerialName("first_image")
    val firstImage: FirstImageDTO?,
    @SerialName("genres")
    val genres: List<Genre>?,
    @SerialName("id")
    val id: Long,
    @SerialName("item_url")
    val itemUrl: String?,
    @SerialName("place")
    val place: EventPlaceDTO?,
    @SerialName("coords")
    val coords: CoordsDTO?,
    @SerialName("poster")
    val poster: Poster?,
    @SerialName("title")
    val title: String?,
    @SerialName("year")
    val year: Int?,
    @SerialName("publication_date")
    val publicationDate:String?
){
    val ageRestriction: String?
        get() = ageRestrictionRaw?.let { element ->
            when (element) {
                is JsonPrimitive -> {
                    when {
                        element.isString -> element.content
                        element.intOrNull != null -> element.int.toString()
                        element.doubleOrNull != null -> element.double.toInt().toString()
                        else -> null
                    }
                }
                else -> null
            }
        }
}

fun ItemDTO.toItem(): Item {
    return Item(
        description = description,
        id = id,
        startDate = year.toString(),
        startTime = null,
        address = address,
        ageRestriction = ageRestriction,
        images = listOf(firstImage?.thumbnails?.x384),
        categories = null,
        price = null,
        itemUrl = itemUrl,
        title = title,
        placeTitle = place?.title,
        lat = place?.coords?.lat ?: coords?.lat,
        lon = place?.coords?.lon ?: coords?.lon,
        isEndless = null,
        bodyText = bodyText,
        publicationDate = publicationDate,
        city = ""
    )
}
