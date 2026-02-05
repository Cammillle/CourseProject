package com.alfabank.homework.courseproject.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDTO(
    @SerialName("link")
    val link: String?,
    @SerialName("name")
    val name: String?
)