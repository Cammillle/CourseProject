package com.alfabank.homework.courseproject.domain.model

data class Place(
    val address: String?,
    val coords: Coords?,
    val id: Int?,
    val isClosed: Boolean?,
    val isStub: Boolean?,
    val title: String?
)