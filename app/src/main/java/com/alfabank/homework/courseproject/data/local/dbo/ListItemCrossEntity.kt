package com.alfabank.homework.courseproject.data.local.dbo

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "list_item_cross",
    primaryKeys = ["listId", "itemId"],
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ListItemCrossEntity(
    val listId: Long,
    val itemId: Long
)