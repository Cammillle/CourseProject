package com.alfabank.homework.courseproject.domain.model

data class Event(
    val dates: List<DateEvent>?,
    val id: Int,
    val place: EventPlace?,
    val description: String?,
    val images: List<ImageEvent>?,
    val categories: List<String>?,
    val title: String?,
    val ageRestriction: String?,
    val price: String?,
    val location: Location?
)

