package com.alfabank.homework.courseproject.data.dto.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventResponseDTO(
    @SerialName("age_restriction")
    val ageRestriction: String?,
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("categories")
    val categories: List<String>?,
    @SerialName("dates")
    val dates: List<DateDTO>?,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("images")
    val images: List<ImageDTO>?,
    @SerialName("is_free")
    val isFree: Boolean?,
    @SerialName("location")
    val location: LocationDTO?,
    @SerialName("place")
    val place: PlaceDTO?,
    @SerialName("price")
    val price: String?,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("tagline")
    val tagline: String?,
    @SerialName("tags")
    val tags: List<String>?,
    @SerialName("title")
    val title: String?
)