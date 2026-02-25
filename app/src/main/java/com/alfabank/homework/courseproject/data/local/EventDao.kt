package com.alfabank.homework.courseproject.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatasetRefs(refs: List<DatasetEventCrossRef>)

    @Query("DELETE FROM dataset_event_cross_ref WHERE datasetKey = :datasetKey")
    suspend fun clearDataset(datasetKey: String)

    @Transaction
    @Query("""
        SELECT * FROM events
        INNER JOIN dataset_event_cross_ref
        ON events.id = dataset_event_cross_ref.eventId
        WHERE dataset_event_cross_ref.datasetKey = :datasetKey
        ORDER BY publicationDate DESC
    """)
    fun pagingSource(datasetKey: String): PagingSource<Int, EventWithRelations>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): EventEntity?
}