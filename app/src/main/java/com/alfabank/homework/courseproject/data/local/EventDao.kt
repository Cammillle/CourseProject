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

    // Запросы для ItemEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getEventById(id: Long): ItemEntity?

    @Query("SELECT * FROM items WHERE id = :id")
    fun observeEventById(id: Long): Flow<ItemEntity?>

    @Query("SELECT * FROM items")
    fun observeAllEvents(): Flow<List<ItemEntity>>

    // Запросы для CategoryEntity
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("SELECT nextUrl FROM categories WHERE id = :categoryId")
    suspend fun getNextUrlForCategory(categoryId: String): String?

    @Query("SELECT nextUrl FROM categories WHERE id = :categoryId")
    fun observeNextUrlForCategory(categoryId: String): Flow<String?>

    // Запросы для связей
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemCategoryCrossRefs(crossRefs: List<ItemCategoryCrossRef>)

    // Получение элементов по категории (для проверки наличия кеша)
    @Query("SELECT items.* FROM items INNER JOIN item_category_cross_ref ON items.id = item_category_cross_ref.itemId WHERE item_category_cross_ref.categoryId = :categoryId")
    suspend fun getItemsByCategory(categoryId: String): List<ItemEntity>

    // Наблюдение за элементами категории
    @Query("SELECT items.* FROM items INNER JOIN item_category_cross_ref ON items.id = item_category_cross_ref.itemId WHERE item_category_cross_ref.categoryId = :categoryId")
    fun observeItemsByCategory(categoryId: String): Flow<List<ItemEntity>>

    // Для постраничной загрузки из БД (если используется Paging 3)
    @Query("SELECT items.* FROM items INNER JOIN item_category_cross_ref ON items.id = item_category_cross_ref.itemId WHERE item_category_cross_ref.categoryId = :categoryId ORDER BY items.id LIMIT :limit OFFSET :offset")
    fun getItemsByCategoryPaged(categoryId: String, limit: Int, offset: Int): List<ItemEntity>

    @Transaction
    @Query("SELECT * FROM items WHERE id = :id")
    fun observeEventWithCategories(id: Long): Flow<ItemWithCategories?>

    @Transaction
    @Query("SELECT * FROM items INNER JOIN item_category_cross_ref ON items.id = item_category_cross_ref.itemId WHERE item_category_cross_ref.categoryId = :categoryId")
    fun observeItemsByCategoryWithCategories(categoryId: String): Flow<List<ItemWithCategories>>

}