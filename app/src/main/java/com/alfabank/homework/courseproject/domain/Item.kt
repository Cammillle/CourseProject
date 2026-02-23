package com.alfabank.homework.courseproject.domain

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,

    val ctype: String?, //event place

    val startDate: String?,
    val startTime: String?,
    val isEndless: Boolean?,

    val address: String?,
    val ageRestriction: String?,
    val description: String?,
    val bodyText: String?,

    val images: List<String?>?,
    val categories: List<String>?,
    val price: String?,

    val itemUrl: String?,
    val title: String?,
    val placeTitle: String?,
    val lat: Double?,
    val lon: Double?,
)