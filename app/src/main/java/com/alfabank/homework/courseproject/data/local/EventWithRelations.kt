package com.alfabank.homework.courseproject.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithRelations(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId",
        entity = EventCategoryCrossRef::class,
        projection = ["category"]
    )
    val categories: List<String>,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId",
        entity = EventImageEntity::class,
        projection = ["imageUrl"]
    )
    val images: List<String>
)