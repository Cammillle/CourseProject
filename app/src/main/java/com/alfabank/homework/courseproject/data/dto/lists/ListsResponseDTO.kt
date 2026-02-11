package com.alfabank.homework.courseproject.data.dto.lists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListsResponseDTO(
    @SerialName("count")
    val count: Int?,
    @SerialName("next")
    val next: String?,
    @SerialName("previous")
    val previous: String?,
    @SerialName("results")
    val listItems: List<ListItemDTO>?
)