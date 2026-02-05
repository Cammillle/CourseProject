package com.alfabank.homework.courseproject.domain.places

import com.alfabank.homework.courseproject.domain.model.Coords
import com.alfabank.homework.courseproject.domain.model.ImageEvent

data class Place(
    val address: String,
    val categories: List<String>,
    val coords: Coords?,
    val description: String,
    val id: Int,
    val images: List<ImageEvent>?,
    val tags: List<String>,
    val title: String
)