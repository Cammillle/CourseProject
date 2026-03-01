package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKeys: List<RemoteKeys>)

    @Query("""SELECT * FROM remote_keys WHERE eventId = :eventId AND category IS :category 
        AND city = :city
            """)
    suspend fun getRemoteKeysEventIdWithCategory(
        eventId: Long,
        category: String,
        city: String
    ): RemoteKeys?

    @Query("""
            SELECT * FROM remote_keys WHERE eventId = :eventId AND category = 'all' 
            and city =:city""")
    suspend fun getRemoteKeysEventIdNoCategory(
        eventId: Long,
        city: String
    ): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()

    @Query("DELETE FROM remote_keys WHERE category IS :category and city =:city")
    suspend fun clearByCategory(
        category: String,
        city: String
    )
}