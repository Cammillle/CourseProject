package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    tableName = "event",
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listId")]
)
@TypeConverters(StringListConverter::class)
data class ItemEntity(
    @PrimaryKey
    val id: Long,

    val listId: Long?,

    val title: String?,
    val placeTitle: String?,
    val description: String?,
    val bodyText: String?,

    val ageRestriction: String?,

    val address: String?,
    val lan: Double?,
    val lon: Double?,

    val images: List<String>?,

    val price: String?,
    val siteUrl: String?,

    val startDate: String?,
    val startTime: String?,

    val categories: List<String>?
)

