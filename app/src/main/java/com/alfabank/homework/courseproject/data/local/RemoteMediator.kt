package com.alfabank.homework.courseproject.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.data.EventsApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator(
    private val api: EventsApi,
    private val db: EventDatabase,
    private val datasetKey: String
) : RemoteMediator<Int, EventWithRelations>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventWithRelations>
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                    ?: return MediatorResult.Success(true)

                remoteKeys.nextKey
                    ?: return MediatorResult.Success(true)
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
        }

        try {
            val response = api.getPopularEventsByCategories(
                categories = if (datasetKey == "NO_FILTER") "" else datasetKey,
                page = page,
                actualSince = today()
            )

            val events = response.results.orEmpty()
            val endOfPaginationReached = events.isEmpty()

            db.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    db.eventDao().clearDataset(datasetKey)
                    db.remoteKeysDao().clearRemoteKeys(datasetKey)
                }

                val keys = events.map {
                    RemoteKeys(
                        eventId = it.id,
                        datasetKey = datasetKey,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }

                db.remoteKeysDao().insertAll(keys)
                db.eventDao().insertEvents(events.map { it.toEventEntity() })
                db.eventDao().insertDatasetRefs(
                    events.map { it.toDatasetRef(datasetKey) }
                )
            }

            return MediatorResult.Success(endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, EventWithRelations>
    ): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { db.remoteKeysDao().remoteKeys(it.event.id, datasetKey) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, EventWithRelations>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.event?.id?.let { id ->
                db.remoteKeysDao().remoteKeys(id, datasetKey)
            }
        }
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
}