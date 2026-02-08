package com.alfabank.homework.courseproject.data

import coil.network.HttpException
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.domain.EventData
import com.alfabank.homework.courseproject.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventRepositoryImpl() {
    private val api = EventsApi()
    private val db = DatabaseProvider.getDatabase()

    private val dao = db.eventDao()

    fun getTodayPopularEventsByCategory(
        fetchFromRemote: Boolean,
        query: String,
        category: String
    ): Flow<Result<EventData>> {
        return flow {
            val localEvents = dao.searchEvents(query)
            emit(Result.success(localEvents.toEventData()))

            val isDbIsEmpty = localEvents.isEmpty() && query.isEmpty()
            val shouldJustLoadFromCache = !isDbIsEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                return@flow
            }
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val remoteData = try {
                if (category.isEmpty() && query.isEmpty()) {
                    api.getEventsWithoutFilters(actualSince = today)
                } else {
                    api.getPopularEventsByCategory1(category, actualSince = today)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.failure(e))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.failure(e))
                null
            }
            remoteData?.let { data ->
                dao.clearEventsList()
                val events = data.toListEvent() ?: emptyList()
                val nextPage = data.next
                dao.insertEventsList(eventDBO = events.map { it.toEventDBO() })
                emit(Result.success(EventData(events, nextPage)))
            }
        }
    }


    suspend fun getEventById(id: Long): Result<Event> {
        return try {
            val response = api.getEventById(id)
            val event = response.toEvent()
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventsWithoutFilters(): Result<EventData> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return try {
            val response = api.getEventsWithoutFilters(actualSince = today)
            val events = response.toListEvent() ?: emptyList()
            val nextPage = response.next
            Result.success(EventData(events = events, nextUrl = nextPage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTodayPopularEventsByCategory1(category: String): Result<EventData> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return try {
            val response = api.getPopularEventsByCategory1(
                actualSince = today,
                categories = category
            )
            val events = response.toListEvent() ?: emptyList()
            val nextPage = response.next
            Result.success(EventData(events = events, nextUrl = nextPage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNextEvents(url: String): Result<EventData> {
        return try {
            val response = api.getNextEvents(url = url)
            val events = response.toListEvent() ?: emptyList()
            val nextPage = response.next
            Result.success(EventData(events = events, nextUrl = nextPage))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}