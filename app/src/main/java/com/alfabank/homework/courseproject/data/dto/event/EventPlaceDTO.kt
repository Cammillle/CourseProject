package com.alfabank.homework.courseproject.data.dto.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventPlaceDTO(
    @SerialName("address")
    val address: String?,
    @SerialName("coords")
    val coords: CoordsDTO?,
    @SerialName("id")
    val id: Int?,
    @SerialName("is_closed")
    val isClosed: Boolean?,
    @SerialName("is_stub")
    val isStub: Boolean?,
    @SerialName("location")
    val location: String?,
    @SerialName("phone")
    val phone: String?,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("subway")
    val subway: String?,
    @SerialName("title")
    val title: String?
)