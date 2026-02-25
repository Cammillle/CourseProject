package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PagingKeysDao {

    @Query("SELECT * FROM paging_keys WHERE datasetKey = :datasetKey")
    suspend fun getKeys(datasetKey: String): PagingKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(keys: PagingKeys)
}