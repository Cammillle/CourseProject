package com.alfabank.homework.courseproject.data.dto.lists

import com.alfabank.homework.courseproject.data.local.ListEntity
import com.alfabank.homework.courseproject.domain.model.Item
import com.alfabank.homework.courseproject.domain.model.ListItem
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

fun ListItemResponseDTO.toListItemEntity(): ListEntity{
    return ListEntity(
        description = description,
        id = id,
        images = images?.map { it.image!! },
        itemUrl = itemUrl,
        siteUrl = siteUrl,
        title = title
    )
}



