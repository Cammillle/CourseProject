package com.alfabank.homework.courseproject.data.dto.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoordsDTO(
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lon")
    val lon: Double?
)