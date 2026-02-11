package com.alfabank.homework.courseproject.data.dto.lists


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListItemImageDTO(
    @SerialName("image")
    val image: String?
)