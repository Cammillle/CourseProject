package com.alfabank.homework.courseproject.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Coords(
    val lat: Double?,
    val lon: Double?
)