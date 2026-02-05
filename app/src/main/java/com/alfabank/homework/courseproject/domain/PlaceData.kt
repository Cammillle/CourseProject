package com.alfabank.homework.courseproject.domain

import com.alfabank.homework.courseproject.domain.places.Place

data class PlaceData(
    val places: List<Place>,
    val nextUrl: String?
)