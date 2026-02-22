package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    // ===== Observe (источник истины) =====

    @Query("SELECT * FROM event")
    suspend fun getAllEvents(): List<ItemEntity>

    @Query("SELECT * FROM event ORDER BY startDate ASC")
    fun observeEvents(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM event WHERE id = :id LIMIT 1")
    fun observeEventById(id: Long): Flow<ItemEntity?>

    @Query("SELECT * FROM event WHERE category = :category")
    suspend fun getEventsByCategory(category: String): List<ItemEntity>

    @Query("SELECT * FROM event WHERE category = :category")
    fun observeEventsByCategory(category: String): Flow<List<ItemEntity>>

    // ===== One-shot =====

    @Query("SELECT * FROM event WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: Long): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: ItemEntity)

    @Query("DELETE FROM event")
    suspend fun clearEvents()

}