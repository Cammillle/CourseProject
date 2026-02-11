package com.alfabank.homework.courseproject.data.dto.lists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListItemsResponseDTO(
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("ctype")
    val ctype: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Long,
    @SerialName("images")
    val images: List<ListItemImageDTO>?,
    @SerialName("item_url")
    val itemUrl: String?,
    @SerialName("items")
    val items: List<Item>?,
    @SerialName("publication_date")
    val publicationDate: Int?,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("title")
    val title: String?
)