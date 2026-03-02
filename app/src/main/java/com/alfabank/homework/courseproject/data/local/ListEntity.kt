package com.alfabank.homework.courseproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alfabank.homework.courseproject.domain.model.Item
import com.alfabank.homework.courseproject.domain.model.ListItem
import kotlin.String

@Entity(tableName = "lists_of_items")
@TypeConverters(StringListConverter::class)
data class ListEntity(
    @PrimaryKey
    val id: Long,
    val title: String?,
    val description: String?,
    val images: List<String>?, // через TypeConverter
    val itemUrl: String?,
    val siteUrl: String?
)

fun ListEntity.toListItem(): ListItem {
    return ListItem(
        ctype = "",
        description = description,
        id = id,
        images = images,
        itemUrl = itemUrl,
        siteUrl = siteUrl,
        title = title
    )
}
