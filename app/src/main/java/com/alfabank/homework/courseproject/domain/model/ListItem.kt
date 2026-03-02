package com.alfabank.homework.courseproject.domain.model

import com.alfabank.homework.courseproject.domain.Item
import kotlinx.serialization.Serializable

@Serializable
data class ListItem(
    val ctype: String?,// list
    val description: String?,
    val id: Long,
    val images: List<String>?,
    val itemUrl: String?,
    val siteUrl: String?,
    val title: String?
)