package com.alfabank.homework.courseproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alfabank.homework.courseproject.data.local.dao.EventDao
import com.alfabank.homework.courseproject.data.local.dao.ListsDao
import com.alfabank.homework.courseproject.data.local.dao.RemoteKeysDao
import com.alfabank.homework.courseproject.data.local.dbo.EventCategoryCrossRef
import com.alfabank.homework.courseproject.data.local.dbo.EventEntity
import com.alfabank.homework.courseproject.data.local.dbo.ItemEntity
import com.alfabank.homework.courseproject.data.local.dbo.ListEntity
import com.alfabank.homework.courseproject.data.local.dbo.ListItemCrossEntity
import com.alfabank.homework.courseproject.data.local.dbo.RemoteKeys


@Database(
    entities = [EventEntity::class,
        RemoteKeys::class,
        EventCategoryCrossRef::class,
        ItemEntity::class,
        ListEntity::class,
        ListItemCrossEntity::class], version = 1
)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun listsDao(): ListsDao
}

fun EventDatabase(applicationContext: Context): EventDatabase {
    return Room.databaseBuilder(
        context = checkNotNull(applicationContext.applicationContext),
        klass = EventDatabase::class.java,
        "events"
    ).build()
}