package com.alfabank.homework.courseproject.data

import coil.network.HttpException
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.local.CategoryEntity
import com.alfabank.homework.courseproject.data.local.ItemCategoryCrossRef
import com.alfabank.homework.courseproject.data.local.toItem
import com.alfabank.homework.courseproject.data.local.toItemEntity
import com.alfabank.homework.courseproject.domain.EventData
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import okio.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventRepositoryImpl {
    private val api = EventsApi()
    private val db = DatabaseProvider.getDatabase()
    private val dao = db.eventDao()

    fun getTodayPopularEventsByCategory(
        query: String,
        category: String
    ): Flow<Result<EventData>> = flow {
        val effectiveCategory = category.ifEmpty { "all" }

        // Проверяем наличие кеша
        val hasCached = dao.getItemsByCategory(effectiveCategory).isNotEmpty()
        if (!hasCached) {
            fetchAndCache(effectiveCategory, query)
        }

        // Комбинируем Flow элементов и Flow nextUrl
        combine(
            dao.observeItemsByCategoryWithCategories(effectiveCategory),
            dao.observeNextUrlForCategory(effectiveCategory)
        ) { entities, nextUrl ->
            Result.success(
                EventData(
                    events = entities.map { it.toItem() },
                    nextUrl = nextUrl
                )
            )
        }.collect { emit(it) }
    }.catch { emit(Result.failure(it)) }


    fun getEventById(id: Long): Flow<Result<Item>> = flow {
        combine(
            dao.observeEventWithCategories(id),
            flow { emit(dao.getEventById(id)) } // начальная загрузка, если нужно
        ) { entity, _ ->
            if (entity != null) {
                Result.success(entity.toItem())
            } else {
                Result.failure(Exception("Event not found in cache"))
            }
        }.collect { emit(it) }
    }.onStart {
        val cached = dao.getEventById(id)
        if (cached == null) {
            try {
                val response = api.getEventById(id)
                val event = response.toItem()
                saveItemsWithCategories(listOf(event), requestCategoryId = null)
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }.catch { emit(Result.failure(it)) }


    fun getEventsWithoutFilters(): Flow<Result<EventData>> = flow {
        val category = "all"
        val hasCached = dao.getItemsByCategory(category).isNotEmpty()
        if (!hasCached) {
            try {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val response = api.getEventsWithoutFilters(actualSince = today)
                val events = response.toListEvent() ?: emptyList()
                val nextUrl = response.next

                saveItemsWithCategories(events, category)
                dao.updateCategory(CategoryEntity(id = category, name = category, nextUrl = nextUrl))
            } catch (e: Exception) {
                emit(Result.failure(e))
                return@flow
            }
        }

        combine(
            dao.observeItemsByCategoryWithCategories(category),
            dao.observeNextUrlForCategory(category)
        ) { entities, nextUrl ->
            Result.success(EventData(entities.map { it.toItem() }, nextUrl))
        }.collect { emit(it) }
    }.catch { emit(Result.failure(it)) }


    suspend fun getNextEvents(category: String, url: String): Result<EventData> {
        return try {
            val response = api.getNextEvents(url)
            val events = response.toListEvent() ?: emptyList()
            val nextUrl = response.next

            saveItemsWithCategories(events, category)
            dao.updateCategory(CategoryEntity(id = category, name = category, nextUrl = nextUrl))

            Result.success(EventData(events, nextUrl))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchAndCache(category: String, query: String) {
        try {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val response = if (category == "all") {
                api.getEventsWithoutFilters(actualSince = today)
            } else {
                api.getPopularEventsByCategory1(category, actualSince = today)
            }
            val events = response.toListEvent() ?: emptyList()
            val nextUrl = response.next

            // Сохраняем данные
            saveItemsWithCategories(events, category)

            // Обновляем nextUrl для категории
            dao.updateCategory(CategoryEntity(id = category, name = category, nextUrl = nextUrl))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun saveItemsWithCategories(
        items: List<Item>,
        requestCategoryId: String? = null
    ) {
        // 1. Сохраняем элементы (ItemEntity)
        val itemEntities = items.map { it.toItemEntity() } // toItemEntity теперь без category
        dao.insertItems(itemEntities)

        // 2. Собираем все уникальные категории из элементов и добавляем категорию запроса (если есть)
        val allCategoryIds = items.flatMap { it.categories ?: emptyList() }.toMutableSet()
        requestCategoryId?.let { allCategoryIds.add(it) }

        // 3. Сохраняем категории (игнорируем существующие)
        val categoryEntities = allCategoryIds.map {
            CategoryEntity(
                id = it,
                name = it,
                nextUrl = null
            )
        }
        dao.insertCategories(categoryEntities)

        // 4. Создаём связи
        val crossRefs = mutableListOf<ItemCategoryCrossRef>()
        items.forEach { item ->
            val itemCategories = item.categories ?: emptyList()
            // Связи для всех категорий элемента
            itemCategories.forEach { catId ->
                crossRefs.add(ItemCategoryCrossRef(item.id, catId))
            }
            // Если категория запроса отсутствует в списке элемента, добавляем отдельную связь
            if (requestCategoryId != null && !itemCategories.contains(requestCategoryId)) {
                crossRefs.add(ItemCategoryCrossRef(item.id, requestCategoryId))
            }
        }
        dao.insertItemCategoryCrossRefs(crossRefs)
    }
}

