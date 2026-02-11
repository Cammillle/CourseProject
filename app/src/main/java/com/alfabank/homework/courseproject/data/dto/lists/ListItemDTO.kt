package com.alfabank.homework.courseproject.data.dto.lists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListItemDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("publication_date")
    val publicationDate: Int,
    @SerialName("title")
    val title: String
)