package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Query("""
        SELECT * FROM remote_keys 
        WHERE eventId = :eventId AND datasetKey = :datasetKey
    """)
    suspend fun remoteKeys(
        eventId: Long,
        datasetKey: String
    ): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys WHERE datasetKey = :datasetKey")
    suspend fun clearRemoteKeys(datasetKey: String)
}