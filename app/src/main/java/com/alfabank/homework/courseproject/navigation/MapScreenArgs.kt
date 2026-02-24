package com.alfabank.homework.courseproject.navigation

import com.alfabank.homework.courseproject.data.dto.lists.ListItem
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.serialization.Serializable

@Serializable
sealed class MapScreenArgs {
    @Serializable
    data class ListData(val listItem: ListItem) : MapScreenArgs()

    @Serializable
    data class SingleItem(val item: Item) : MapScreenArgs()
}