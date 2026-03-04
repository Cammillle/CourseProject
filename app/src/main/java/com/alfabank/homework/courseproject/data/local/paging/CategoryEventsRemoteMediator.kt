package com.alfabank.homework.courseproject.data.local.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alfabank.homework.courseproject.api.EventsApi
import com.alfabank.homework.courseproject.data.local.dbo.EventCategoryCrossRef
import com.alfabank.homework.courseproject.data.local.EventDatabase
import com.alfabank.homework.courseproject.data.local.dbo.EventEntity
import com.alfabank.homework.courseproject.data.local.dbo.RemoteKeys
import com.alfabank.homework.courseproject.data.local.toEventEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPagingApi::class)
class CategoryEventsRemoteMediator(
    private val api: EventsApi,
    private val db: EventDatabase,
    private val category: String,
    private val city: String
) : RemoteMediator<Int, EventEntity>() {
    private val eventsDao = db.eventsDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val hasData = eventsDao.getEventsCountByCategory(category, city) > 0
        return if (hasData) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        Log.d("EventsRemoteMediator", "MEDIATOR FIRED: $loadType")
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys =
                        getRemoteKeyClosestToCurrentPositionWithCategory(state, category, city)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItemWithCategory(state, category, city)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItemWithCategory(state, category, city)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            // ---------- API ----------
            val response = api.getPopularEventsByCategories(
                actualSince = today(),
                page = page,
                categories = category,
                location = city
            )
            val events = response.results?.let {
                it.map { dto ->
                    //При обновлении из сети, чтобы не перезаписывался флаг isFavourite
                    val isFavourite = eventsDao.getEventById(dto.id)?.isFavourite
                    dto.toEventEntity(isFavourite)
                }
            } ?: emptyList()
            val endOfPaginationReached = events.isEmpty() || response.next == null

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearByCategory(category, city) //удаление ключей
                    //eventsDao.clearCrossRefs() //удаление старых связей
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = events.map {
                    RemoteKeys(
                        eventId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        category = category,
                        city = city
                    )
                }
                val crossRefs = events.map {
                    EventCategoryCrossRef(
                        eventId = it.id,
                        category = category,
                    )
                }
                remoteKeysDao.insertAllKeys(keys)
                eventsDao.insertEvents(events)
                eventsDao.insertCategoryCrossRefs(crossRefs)
            }
            return MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyForLastItemWithCategory(
        state: PagingState<Int, EventEntity>,
        category: String,
        city: String
    ): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { event ->
                remoteKeysDao.getRemoteKeysEventIdWithCategory(event.id, category, city)
            }
    }


    private suspend fun getRemoteKeyForFirstItemWithCategory(
        state: PagingState<Int, EventEntity>,
        category: String,
        city: String
    ): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { event ->
                remoteKeysDao.getRemoteKeysEventIdWithCategory(event.id, category, city)
            }
    }


    private suspend fun getRemoteKeyClosestToCurrentPositionWithCategory(
        state: PagingState<Int, EventEntity>,
        category: String,
        city: String
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeysDao.getRemoteKeysEventIdWithCategory(repoId, category, city)
            }
        }
    }


    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
}
