package com.alfabank.homework.courseproject.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailsDTO(
    @SerialName("640x384")
    val x384: String?,
    @SerialName("144x96")
    val x96: String?
)