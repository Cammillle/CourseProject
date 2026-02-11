package com.alfabank.homework.courseproject.data.dto.lists


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Poster(
    @SerialName("image")
    val image: String?,
    @SerialName("source")
    val source: Source?
)