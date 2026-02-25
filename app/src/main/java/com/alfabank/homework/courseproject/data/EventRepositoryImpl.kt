package com.alfabank.homework.courseproject.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.local.EventsRemoteMediator
import com.alfabank.homework.courseproject.data.local.toDomain
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EventRepositoryImpl {
    private val api = EventsApi()
    private val db = DatabaseProvider.getDatabase()
    private val dao = db.eventDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getEvents(datasetKey: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = EventsRemoteMediator(
                api, db, datasetKey,
                eventDao = dao
            ),
            pagingSourceFactory = {
                db.eventDao().pagingSource(datasetKey)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }


//    fun observeEvents(category: String): Flow<Result<EventData>> {
//
//        val effectiveCategory = category.ifEmpty { "all" }
//
//        return flow {
//
//            val shouldFetch = shouldFetch(effectiveCategory)
//
//            if (shouldFetch) {
//                fetchFirstPage(effectiveCategory)
//            }
//
//            emitAll(
//                combine(
//                    dao.observeItemsWithCategories(effectiveCategory),
//                    dao.observeNextUrl(effectiveCategory)
//                ) { items, nextUrl ->
//                    Result.success(
//                        EventData(
//                            events = items.map { it.toItem() },
//                            nextUrl = nextUrl
//                        )
//                    )
//                }
//            )
//        }.catch { emit(Result.failure(it)) }
//    }
//
//    suspend fun loadNextPage(category: String): Result<Unit> {
//        val effectiveCategory = category.ifEmpty { "all" }
//
//        val nextUrl = dao.getNextUrl(effectiveCategory)
//            ?: return Result.success(Unit)
//
//        Log.d("TAGATaAG", "$category")
//        Log.d("TAGATaAG", "$nextUrl")
//        return try {
//            val response = api.getNextEvents(nextUrl)
//            val events = response.toListEvent() ?: emptyList()
//
//            savePage(
//                events, effectiveCategory, response.next,
//                updateTimestamp = true
//            )
//
//            Result.success(Unit)
//
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    fun observeEventById(id: Long): Flow<Result<Item>> {
//
//        Log.d("TAGTAG", "Event by id ")
//        return flow {
//
//            val cached = dao.getEventById(id)
//            Log.d("TAGTAG", "Cached $cached ")
//
//            if (cached == null) {
//                fetchAndCacheEvent(id)
//            }
//
//            emitAll(
//                dao.observeEventWithCategories(id)
//                    .map { entity ->
//                        Log.d("TAGTAG", "Item $entity ")
//                        if (entity != null) {
//                            Log.d("TAGTAG", "Item ${entity.toItem()} ")
//                            Result.success(entity.toItem())
//                        } else {
//                            Result.failure(Exception("Event not found"))
//                        }
//                    }
//            )
//        }.catch { emit(Result.failure(it)) }
//    }
//
//    private suspend fun fetchAndCacheEvent(id: Long) {
//
//        try {
//            val response = api.getEventById(id)
//            val event = response.toItem()
//
//            val entity = event.toItemEntity()
//
//            dao.insertItems(listOf(entity))
//
//            val refs = (event.categories ?: emptyList()).map { categoryId ->
//                ItemCategoryCrossRef(
//                    itemId = event.id,
//                    categoryId = categoryId
//                )
//            }
//
//            dao.insertCrossRefs(refs)
//
//        } catch (e: Exception) {
//            throw e
//        }
//    }
//
//    private suspend fun fetchFirstPage(category: String) {
//
//        val today = today()
//
//        val response = if (category == "all") {
//            api.getEventsWithoutFilters(today, page = 1)
//        } else {
//            api.getPopularEventsByCategories(categories = category, actualSince = today, page = 1)
//        }
//
//        val events = response.toListEvent() ?: emptyList()
//
//        savePage(
//            events = events,
//            category = category,
//            nextUrl = response.next,
//            updateTimestamp = true
//        )
//    }
//
//    private suspend fun savePage(
//        events: List<Item>,
//        category: String,
//        nextUrl: String?,
//        updateTimestamp: Boolean = false
//    ) {
//        val itemEntities = events.map { it.toItemEntity() }
//
//        val now = System.currentTimeMillis()
//        val existingLastUpdated = dao.getLastUpdated(category)
//
//        val categoryEntity = CategoryEntity(
//            id = category,
//            name = category,
//            nextUrl = nextUrl,
//            lastUpdated = if (updateTimestamp || existingLastUpdated == null)
//                now
//            else
//                existingLastUpdated
//        )
//
//        if (existingLastUpdated == null) {
//            dao.insertCategory(categoryEntity)
//        } else {
//            dao.updateCategory(categoryEntity)
//        }
//
//        val refs = events.map {
//            ItemCategoryCrossRef(
//                itemId = it.id,
//                categoryId = category
//            )
//        }
//
//        dao.insertItems(itemEntities)
//        dao.insertCrossRefs(refs)
//    }
//
//    private suspend fun shouldFetch(category: String): Boolean {
//
//        val items = dao.getItemsByCategory(category)
//        if (items.isEmpty()) return true
//
//        val lastUpdated = dao.getLastUpdated(category)
//            ?: return true
//
//        return System.currentTimeMillis() - lastUpdated > ttl
//    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
}

