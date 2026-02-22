package com.alfabank.homework.courseproject.data

import coil.network.HttpException
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.local.toItem
import com.alfabank.homework.courseproject.data.local.toItemEntity
import com.alfabank.homework.courseproject.domain.EventData
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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

        val cachedEvents = dao.getEventsByCategory(category)

        if (cachedEvents.isEmpty()) {
            fetchAndCache(category, query)
        }

        emitAll(
            dao.observeEventsByCategory(category).map { entities ->
                Result.success(
                    EventData(
                        events = entities.map { it.toItem() },
                        nextUrl = null
                    )
                )
            }
        )
    }.catch {
        emit(Result.failure(exception = it))
    }


    fun getEventById(id: Long): Flow<Result<Item>> = flow {
        emitAll(
            dao.observeEventById(id).map { entity ->
                if (entity != null) {
                    Result.success(entity.toItem())
                } else {
                    Result.failure(Exception("Event not found in cache"))
                }
            }
        )
    }.onStart {

        val cached = dao.getEventById(id)

        if (cached == null) {
            try {
                val response = api.getEventById(id)
                val event = response.toItem()
                dao.insertEvent(event.toItemEntity(event.categories?.get(0)!!))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }.catch {
        emit(Result.failure(exception = it))
    }

    fun getEventsWithoutFilters(): Flow<Result<EventData>> = flow {
        val cachedEvents = dao.getAllEvents()

        if (cachedEvents.isEmpty()) {
            try {
                val today = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())

                val response = api.getEventsWithoutFilters(actualSince = today)

                val events = response.toListEvent() ?: emptyList()
                val nextPage = response.next

                dao.insertEvents(events.map { it.toItemEntity(it.categories?.get(0)!!) })

                emit(Result.success(EventData(events, nextPage)))

            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
        emitAll(
            dao.observeEvents().map { entities ->
                Result.success(
                    EventData(
                        events = entities.map { it.toItem() },
                        nextUrl = null
                    )
                )
            }
        )
    }.catch {
        emit(Result.failure(it))
    }


    suspend fun getNextEvents(url: String): Result<EventData> {
        return try {

            val response = api.getNextEvents(url = url)

            val events = response.toListEvent() ?: emptyList()
            val nextPage = response.next

            dao.insertEvents(events.map { it.toItemEntity(category = it.categories?.get(0)!!) })

            Result.success(
                EventData(
                    events = events,
                    nextUrl = nextPage
                )
            )

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchAndCache(
        category: String,
        query: String
    ) {
        try {
            val today = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())

            val response = if (category.isEmpty() && query.isEmpty()) {
                api.getEventsWithoutFilters(actualSince = today)
            } else {
                api.getPopularEventsByCategory1(
                    category,
                    actualSince = today
                )
            }

            val events = response.toListEvent() ?: emptyList()

            dao.insertEvents(events.map { it.toItemEntity(category) })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

