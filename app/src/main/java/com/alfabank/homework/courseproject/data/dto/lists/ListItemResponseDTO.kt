package com.alfabank.homework.courseproject.data.dto.lists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListItemResponseDTO(
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
    val items: List<ItemDTO>?,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("title")
    val title: String?
)

fun ListItemResponseDTO.toListItemResponse(): ListItemResponse{
    return ListItemResponse(
        ctype = ctype,
        description = description,
        id = id,
        images = images?.map { it.image!! },
        itemUrl = itemUrl,
        items = items?.map { it.toItem() },
        siteUrl = siteUrl,
        title = title
    )
}

data class ListItemResponse(
    val ctype: String?,
    val description: String?,
    val id: Long,
    val images: List<String>?,
    val itemUrl: String?,
    val items: List<Item>?,
    val siteUrl: String?,
    val title: String?
)