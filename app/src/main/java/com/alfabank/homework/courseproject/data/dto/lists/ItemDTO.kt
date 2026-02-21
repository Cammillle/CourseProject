package com.alfabank.homework.courseproject.data.dto.lists

import com.alfabank.homework.courseproject.data.dto.EventPlaceDTO
import com.alfabank.homework.courseproject.data.toPlace
import com.alfabank.homework.courseproject.domain.model.EventPlace
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDTO(
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("ctype")
    val ctype: String?,
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
    @SerialName("poster")
    val poster: Poster?,
    @SerialName("title")
    val title: String?,
    @SerialName("year")
    val year: Int?
)

fun ItemDTO.toItem(): Item{
    return Item(
        ctype = ctype,
        description = description,
        firstImage = firstImage?.image,
        id = id,
        itemUrl = itemUrl,
        place = place?.toPlace(),
        title = title,
        year = year
    )
}

@Serializable
data class Item(
    val ctype: String?,
    val description: String?,
    val firstImage: String?,
    val id: Long,
    val itemUrl: String?,
    val place: EventPlace?,
    val title: String?,
    val year: Int?
)