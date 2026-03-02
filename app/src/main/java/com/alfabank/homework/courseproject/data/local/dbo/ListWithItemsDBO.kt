package com.alfabank.homework.courseproject.data.local.dbo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.alfabank.homework.courseproject.domain.model.ListWithItems

data class ListWithItemsDBO(
    @Embedded val listResponse: ListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ListItemCrossEntity::class,
            parentColumn = "listId",
            entityColumn = "itemId"
        )
    )
    val items: List<ItemEntity>
)

fun ListWithItemsDBO.toListWithItems(): ListWithItems{
    return ListWithItems(
        listItem = listResponse.toListItem(),
        items = items.map { it.toItem() }
    )
}