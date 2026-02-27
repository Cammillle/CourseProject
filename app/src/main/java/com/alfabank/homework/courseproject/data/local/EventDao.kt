package com.alfabank.homework.courseproject.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EventDao {

    // ---------- Paging ----------

    @Query("""
        SELECT e.* FROM events e
        INNER JOIN event_category_cross_ref c
        ON e.id = c.eventId
        WHERE c.category = "all"
        ORDER BY e.publicationDate DESC
    """)
    fun pagingSourceAll(): PagingSource<Int, EventEntity>

    @Query("""
        SELECT e.* FROM events e
        INNER JOIN event_category_cross_ref c
        ON e.id = c.eventId
        WHERE c.category = :category
        ORDER BY e.publicationDate DESC
    """)
    fun pagingSourceByCategory(category: String): PagingSource<Int, EventEntity>


    // ---------- Insert ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryCrossRefs(refs: List<EventCategoryCrossRef>)


    // ---------- Clear ----------

    @Query("DELETE FROM events")
    suspend fun clearAll()

    @Query("""
        DELETE FROM events 
        WHERE id IN (
            SELECT eventId FROM event_category_cross_ref 
            WHERE category = :category
        )
    """)
    suspend fun clearByCategory(category: String)

    @Query("DELETE FROM event_category_cross_ref")
    suspend fun clearCrossRefs()

    @Query("""
        SELECT с.* FROM events с
        where id = :id
    """)
    suspend fun getEventById(id: Long): EventEntity?
}