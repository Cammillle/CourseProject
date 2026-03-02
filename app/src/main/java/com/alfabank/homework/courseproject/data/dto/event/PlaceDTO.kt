package com.alfabank.homework.courseproject.data.dto.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDTO(
    @SerialName("address")
    val address: String?,
    @SerialName("categories")
    val categories: List<String>?,
    @SerialName("coords")
    val coords: CoordsDTO?,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("images")
    val images: List<ImageDTO>?,
    @SerialName("tags")
    val tags: List<String>?,
    @SerialName("title")
    val title: String?,
    @SerialName("is_free")
    val isFree: Boolean?,
    @SerialName("timetable")
    val timetable: String?
)