package com.alfabank.homework.courseproject.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ItemWithCategories(
    @Embedded val item: ItemEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ItemCategoryCrossRef::class,
            parentColumn = "itemId",
            entityColumn = "categoryId"
        )
    )
    val categories: List<CategoryEntity>
)