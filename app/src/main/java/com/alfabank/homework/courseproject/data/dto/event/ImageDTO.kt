package com.alfabank.homework.courseproject.data.dto.event


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDTO(
    @SerialName("image")
    val image: String?,
    @SerialName("source")
    val source: SourceDTO?,
    @SerialName("thumbnails")
    val thumbnails: ThumbnailsDTO?
)