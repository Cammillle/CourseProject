package com.alfabank.homework.courseproject.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alfabank.homework.courseproject.data.local.dbo.EventCategoryCrossRef
import com.alfabank.homework.courseproject.data.local.dbo.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    // ---------- Paging ----------

    @Query(
        """
        SELECT e.* FROM events e
        INNER JOIN event_category_cross_ref c
        ON e.id = c.eventId
        WHERE c.category = "all"
        and e.city = :city
        ORDER BY e.publicationDate DESC
    """
    )
    fun pagingSourceAll(city: String): PagingSource<Int, EventEntity>

    @Query(
        """
        SELECT e.* FROM events e
        INNER JOIN event_category_cross_ref c
        ON e.id = c.eventId
        WHERE c.category = :category
        and e.city = :city
        ORDER BY e.publicationDate DESC
    """
    )
    fun pagingSourceByCategory(
        category: String,
        city: String
    ): PagingSource<Int, EventEntity>

    @Query(  """
        SELECT COUNT(*) FROM events e
        INNER JOIN event_category_cross_ref c
        ON e.id = c.eventId
        WHERE c.category = "all"
        and e.city =:city
    """)
    suspend fun getEventsCount(city: String): Int

    @Query(
        """
        SELECT COUNT(*) FROM events e
        INNER JOIN event_category_cross_ref c
        ON e.id = c.eventId
        WHERE c.category = :category
        and e.city =:city
    """
    )
    suspend fun getEventsCountByCategory(category: String, city: String): Int

    // ---------- Insert ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryCrossRefs(refs: List<EventCategoryCrossRef>)


    // ---------- Clear ----------

    @Query("DELETE FROM events")
    suspend fun clearAllEvents()

    @Query(
        """
        DELETE FROM events 
        WHERE id IN (
            SELECT eventId FROM event_category_cross_ref 
            WHERE category = :category
        ) and events.city = :city
    """
    )
    suspend fun clearByCategory(category: String, city: String)

    @Query("DELETE FROM event_category_cross_ref")
    suspend fun clearCrossRefs()

    //---------------Get--------------
    @Query(
        """
        SELECT с.* FROM events с
        where id = :id
    """
    )
    suspend fun getEventById(id: Long): EventEntity?


    //------------Search----------------------
    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%'")
    fun searchEvent(query: String): Flow<List<EventEntity>>


    //---Favorite----
    @Query("UPDATE events SET isFavourite = :isFavorite WHERE id = :eventId")
    suspend fun updateFavorite(eventId: Long, isFavorite: Boolean)

    @Query("select * from events where isFavourite = 1")
    fun getFavouriteEvents(): Flow<List<EventEntity>>

}