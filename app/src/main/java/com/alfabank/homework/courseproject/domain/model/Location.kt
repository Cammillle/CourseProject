package com.alfabank.homework.courseproject.domain.model

data class Location(
    val coords: Coords?,
    val currency: String?,
    val language: String?,
    val name: String?,
    val slug: String?,
    val timezone: String?
)