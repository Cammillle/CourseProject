package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventsList(
        eventDBO: List<ItemEntity>
    )

    @Query("DELETE FROM event")
    suspend fun clearEventsList()

    @Query(
        """
            SELECT * 
            FROM event
            WHERE LOWER(title) LIKE '%' || LOWER(:query)
        """
    )
    suspend fun searchEvents(query: String): List<ItemEntity>

    @Query("select * from event")
    suspend fun getEvents():List<ItemEntity>

}