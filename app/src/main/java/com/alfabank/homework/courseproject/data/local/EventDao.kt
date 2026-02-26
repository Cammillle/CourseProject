package com.alfabank.homework.courseproject.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EventDao {

    @Query("SELECT * FROM events WHERE queryId = :queryId ORDER BY publicationDate DESC")
    fun getEventsByQueryId(queryId: String): PagingSource<Int, EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE queryId = :queryId")
    suspend fun deleteEventsByQueryId(queryId: String)

    @Query("DELETE FROM paging_metadata WHERE queryId = :queryId")
    suspend fun deleteMetadata(queryId: String)

    @Query("SELECT * FROM paging_metadata WHERE queryId = :queryId")
    suspend fun getMetadata(queryId: String): PagingMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: PagingMetadataEntity)

    @Transaction
    suspend fun savePage(queryId: String, events: List<EventEntity>, nextUrl: String?) {
        events.forEach { insertEvent(it) }
        val metadata = getMetadata(queryId) ?: PagingMetadataEntity(queryId, nextUrl, nextUrl == null)
        if (metadata.nextUrl != nextUrl) {
            insertMetadata(metadata.copy(nextUrl = nextUrl, isEndReached = nextUrl == null))
        }
    }

    @Transaction
    suspend fun clearQueryData(queryId: String) {
        deleteEventsByQueryId(queryId)
        deleteMetadata(queryId)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)


    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): EventEntity?
}