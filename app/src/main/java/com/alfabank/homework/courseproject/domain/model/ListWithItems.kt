package com.alfabank.homework.courseproject.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListWithItems(
    val listItem: ListItem,
    val items: List<Item>
)