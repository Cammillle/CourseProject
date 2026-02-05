package com.alfabank.homework.courseproject.data

import com.alfabank.homework.courseproject.domain.EventData
import com.alfabank.homework.courseproject.domain.EventRepository

class EventRepositoryImpl : EventRepository {
    private val api = EventsApi()

    override suspend fun getAllEvents(pageSize: Int): Result<EventData> {
        return try {
            val response = api.getAllEvents()
            val events = response.toListEvent() ?: emptyList()
            val nextPage = response.next
            Result.success(EventData(events = events, nextUrl = nextPage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsByCategory(
        pageSize: Int,
        category: String
    ): Result<EventData> {
        return try {
            val response = api.getEventsByCategories(categories = category)
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