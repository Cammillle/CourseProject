package com.alfabank.homework.courseproject.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alfabank.homework.courseproject.data.EventsApi
import com.alfabank.homework.courseproject.data.dto.ListOfEventsResponseDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator(
    private val category: String?,
    private val api: EventsApi,
    private val db: EventDatabase
) : RemoteMediator<Int, EventEntity>() {

    private val eventsDao = db.eventsDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {

        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(true)

                    val remoteKeys = remoteKeysDao
                        .remoteKeysEventId(lastItem.id, category)

                    remoteKeys?.nextKey
                        ?: return MediatorResult.Success(true)
                }
            }

            // ---------- API ----------

            val response = if (category == null) {
                api.getEventsWithoutFilters(
                    actualSince = today(),
                    page = page
                )
            } else {
                api.getPopularEventsByCategories(
                    categories = category,
                    actualSince = today(),
                    page = page
                )
            }

            val events = response.results.orEmpty()
            val endOfPaginationReached = response.next == null

            db.withTransaction {

                if (loadType == LoadType.REFRESH) {

                    if (category == null) {
                        remoteKeysDao.clearAll()
                        eventsDao.clearAll()
                        eventsDao.clearCrossRefs()
                    } else {
                        remoteKeysDao.clearByCategory(category)
                        eventsDao.clearByCategory(category)
                    }
                }

                val entities = events.map { dto ->
                    dto.toEventEntity()
                }

                eventsDao.insertEvents(entities)

                val crossRefs = events.flatMap { dto ->
                    dto.categories.orEmpty().map { cat ->
                        EventCategoryCrossRef(
                            eventId = dto.id,
                            category = cat
                        )
                    }
                }

                eventsDao.insertCategoryCrossRefs(crossRefs)

                val keys = events.map { dto ->
                    RemoteKeys(
                        eventId = dto.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1,
                        category = category
                    )
                }

                remoteKeysDao.insertAll(keys)
            }

            return MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
}