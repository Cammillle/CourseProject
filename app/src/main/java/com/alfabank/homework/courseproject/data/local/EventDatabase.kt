package com.alfabank.homework.courseproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [EventEntity::class, CategoryEntity::class,
        EventImageEntity::class,PagingKeys::class,
        RemoteKeys::class,
        DatasetEventCrossRef::class,
        EventCategoryCrossRef::class], version = 1
)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun pagingKeysDao(): PagingKeysDao
}

fun EventDatabase(applicationContext: Context): EventDatabase {
    return Room.databaseBuilder(
        context = checkNotNull(applicationContext.applicationContext),
        klass = EventDatabase::class.java,
        "events"
    ).build()
}