package com.alfabank.homework.courseproject.data.dto.event


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDTO(
    @SerialName("link")
    val link: String?,
    @SerialName("name")
    val name: String?
)