package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "item_category_cross_ref",
    primaryKeys = ["itemId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemCategoryCrossRef(
    val itemId: Long,
    val categoryId: String
)