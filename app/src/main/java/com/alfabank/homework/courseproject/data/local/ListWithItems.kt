package com.alfabank.homework.courseproject.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class ListWithItems(
    @Embedded val list: ListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ItemEntity>
)