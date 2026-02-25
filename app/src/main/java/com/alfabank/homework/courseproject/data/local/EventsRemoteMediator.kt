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
    private val api: EventsApi,
    private val db: EventDatabase,
    private val queryId: String,
    private val categories: List<String>
) : RemoteMediator<Int, EventEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    db.eventDao().clearQueryData(queryId)
                    loadPage(1)
                }

                LoadType.APPEND -> {
                    val metadata = db.eventDao().getMetadata(queryId)
                    if (metadata?.isEndReached == true || metadata?.nextUrl == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    loadPageFromUrl(metadata.nextUrl)
                }

                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun loadPage(page: Int): MediatorResult {
        val actualSince = getCurrentDate()
        val response = if (categories.isEmpty()) {
            api.getEventsWithoutFilters(actualSince = actualSince, page = page)
        } else {
            api.getPopularEventsByCategories(
                categories = categories.joinToString(","),
                page = page,
                actualSince = actualSince
            )
        }
        savePage(response)
        return MediatorResult.Success(endOfPaginationReached = response.next == null)
    }

    private suspend fun loadPageFromUrl(url: String): MediatorResult {
        val response = api.getNextEvents(url)
        savePage(response)
        return MediatorResult.Success(endOfPaginationReached = response.next == null)
    }

    private suspend fun savePage(response: ListOfEventsResponseDTO) {
        val events = response.results ?: emptyList()
        val entities = events.mapNotNull { dto -> dto.toEventEntity(dto, queryId) }
        db.eventDao().savePage(queryId, entities, response.next)
    }

    private fun getCurrentDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun formatDate(timestamp: Long): String =
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(timestamp * 1000))

    private fun formatTime(timestamp: Long): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp * 1000))

}