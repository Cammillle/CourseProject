package com.alfabank.homework.courseproject.data.dto.lists


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("link")
    val link: String?,
    @SerialName("name")
    val name: String?
)