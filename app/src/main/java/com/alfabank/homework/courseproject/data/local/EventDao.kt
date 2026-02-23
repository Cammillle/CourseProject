package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Transaction
    suspend fun insertPageData(
        items: List<ItemEntity>,
        category: CategoryEntity,
        refs: List<ItemCategoryCrossRef>
    ) {
        // 1. Insert the category first so the foreign key exists
        insertCategory(category)
        // 2. Insert items
        insertItems(items)
        // 3. Insert refs (now the categoryId and itemId both exist)
        insertCrossRefs(refs)
    }

    // ---------- Category ----------

    @Query("SELECT lastUpdated FROM categories WHERE id = :categoryId")
    suspend fun getLastUpdated(categoryId: String): Long?

    @Query("SELECT nextUrl FROM categories WHERE id = :categoryId")
    suspend fun getNextUrl(categoryId: String): String?

    @Query("SELECT nextUrl FROM categories WHERE id = :categoryId")
    fun observeNextUrl(categoryId: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)


    // ---------- Items ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRefs(refs: List<ItemCategoryCrossRef>)

    @Query("""
        SELECT items.* FROM items
        INNER JOIN item_category_cross_ref
        ON items.id = item_category_cross_ref.itemId
        WHERE item_category_cross_ref.categoryId = :categoryId
        ORDER BY items.startDate ASC
    """)
    fun observeItemsByCategory(categoryId: String): Flow<List<ItemEntity>>

    @Query("""
        SELECT items.* FROM items
        INNER JOIN item_category_cross_ref
        ON items.id = item_category_cross_ref.itemId
        WHERE item_category_cross_ref.categoryId = :categoryId
    """)
    suspend fun getItemsByCategory(categoryId: String): List<ItemEntity>

    @Transaction
    @Query("""
        SELECT * FROM items
    INNER JOIN item_category_cross_ref ON items.id = item_category_cross_ref.itemId
    WHERE item_category_cross_ref.categoryId = :categoryId
    ORDER BY items.id DESC 
    """)
    fun observeItemsWithCategories(categoryId: String): Flow<List<ItemWithCategories>>

    @Transaction
    @Query("SELECT * FROM items WHERE id = :id")
    fun observeEventWithCategories(id: Long): Flow<ItemWithCategories?>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getEventById(id: Long): ItemEntity?
}