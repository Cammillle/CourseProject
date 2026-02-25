package com.alfabank.homework.courseproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull

@Serializable
data class EventDTO(
    @SerialName("title")
    val title: String?,
    @SerialName("age_restriction")
    val ageRestrictionRaw: JsonElement?,
    @SerialName("price")
    val price: String?,
    @SerialName("dates")
    val dates: List<DateDTO>?,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Long,
    @SerialName("images")
    val images: List<ImageDTO>?,
    @SerialName("location")
    val location: LocationDTO?,
    @SerialName("place")
    val place: EventPlaceDTO?,
    @SerialName("categories")
    val categories: List<String>?,
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("publication_date")
    val publicationDate:String?
){
    val ageRestriction: String?
        get() = ageRestrictionRaw?.let { element ->
            when (element) {
                is JsonPrimitive -> {
                    when {
                        element.isString -> element.content
                        element.intOrNull != null -> element.int.toString()
                        element.doubleOrNull != null -> element.double.toInt().toString()
                        else -> null
                    }
                }
                else -> null
            }
        }
}