package com.alfabank.homework.courseproject.domain.model

import com.alfabank.homework.courseproject.domain.Item
import kotlinx.serialization.Serializable

@Serializable
data class ListWithItems(
    val listEntity: ListItem,
    val items: List<Item>
)