package com.alfabank.homework.courseproject.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ListWithItems(
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