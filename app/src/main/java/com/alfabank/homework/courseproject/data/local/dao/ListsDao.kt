package com.alfabank.homework.courseproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.alfabank.homework.courseproject.data.local.dbo.ItemEntity
import com.alfabank.homework.courseproject.data.local.dbo.ListEntity
import com.alfabank.homework.courseproject.data.local.dbo.ListItemCrossEntity
import com.alfabank.homework.courseproject.data.local.dbo.ListWithItemsDBO

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

    ///----------CLEAR-----------------

    @Query("DELETE FROM lists_of_items")
    suspend fun clearAllLists()

    @Query("DELETE FROM items")
    suspend fun clearItems()

    @Query("DELETE FROM list_item_cross")
    suspend fun clearListCrossReferences()
}