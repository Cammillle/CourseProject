package com.alfabank.homework.courseproject.navigation

import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.domain.model.ListWithItems
import kotlinx.serialization.Serializable

@Serializable
sealed class MapScreenArgs {
    @Serializable
    data class ListData(val listItem: ListWithItems) : MapScreenArgs()

    @Serializable
    data class SingleItem(val item: Item) : MapScreenArgs()
}