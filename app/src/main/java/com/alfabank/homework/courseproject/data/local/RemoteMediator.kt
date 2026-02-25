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
    private val datasetKey: String,
    private val eventDao: EventDao
) : RemoteMediator<Int, EventWithRelations>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventWithRelations>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.APPEND -> state.pages.size + 1
            LoadType.PREPEND -> return MediatorResult.Success(true)
        }

        val response = api.getPopularEventsByCategories(
            categories = if (datasetKey == "NO_FILTER") "" else datasetKey,
            page = page,
            actualSince = today()
        )

        val events = response.results.orEmpty()

        db.withTransaction {

            if (loadType == LoadType.REFRESH) {
                // 💥 очищаем ТОЛЬКО этот dataset
                eventDao.clearDataset(datasetKey)
                eventDao.clearRemoteKeys(datasetKey)
            }

            eventDao.insertEvents(events.map { it.toEventEntity() })
            eventDao.insertCategories(events.flatMap { it.toCategoryRefs() })
            eventDao.insertImages(events.flatMap { it.toImageEntities() })
            eventDao.insertDatasetRefs(events.map { it.toDatasetRef(datasetKey) })
        }

        return MediatorResult.Success(
            endOfPaginationReached = events.isEmpty()
        )
    }
}

private fun today(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date())