package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alfabank.homework.courseproject.domain.model.Event
import retrofit2.http.GET

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventsList(
        eventDBO: List<EventDBO>
    )

    @Query("DELETE FROM events_list")
    suspend fun clearEventsList()

    @Query(
        """
            SELECT * 
            FROM events_list
            WHERE LOWER(title) LIKE '%' || LOWER(:query)
        """
    )
    suspend fun searchEvents(query: String): List<EventDBO>

    @Query("select * from events_list")
    suspend fun getEvents():List<EventDBO>

}