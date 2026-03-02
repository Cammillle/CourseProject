package com.alfabank.homework.courseproject.data.dto.event


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    @SerialName("coords")
    val coords: CoordsDTO?,
    @SerialName("currency")
    val currency: String?,
    @SerialName("language")
    val language: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("timezone")
    val timezone: String?
)