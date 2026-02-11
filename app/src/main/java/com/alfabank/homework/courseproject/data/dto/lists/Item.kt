package com.alfabank.homework.courseproject.data.dto.lists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("age_restriction")
    val ageRestriction: Int?,
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("comments_count")
    val commentsCount: Int?,
    @SerialName("country")
    val country: String?,
    @SerialName("ctype")
    val ctype: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("disable_comments")
    val disableComments: Boolean?,
    @SerialName("favorites_count")
    val favoritesCount: Int?,
    @SerialName("first_image")
    val firstImage: FirstImage?,
    @SerialName("genres")
    val genres: List<Genre>?,
    @SerialName("id")
    val id: Long,
    @SerialName("item_url")
    val itemUrl: String?,
    @SerialName("place")
    val place: Place?,
    @SerialName("poster")
    val poster: Poster?,
    @SerialName("title")
    val title: String?,
    @SerialName("year")
    val year: Int?
)