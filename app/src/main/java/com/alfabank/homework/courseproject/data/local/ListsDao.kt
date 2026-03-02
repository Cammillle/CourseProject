package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListResponse(list: ListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(crossList: List<ListItemCrossEntity>)

    @Transaction
    @Query("SELECT * FROM lists_of_items WHERE id = :listId")
    suspend fun getListWithItems(listId: Long): ListWithItemsDBO?
}