package com.alfabank.homework.courseproject.data

import com.alfabank.homework.courseproject.domain.EventData
import com.alfabank.homework.courseproject.domain.model.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventRepositoryImpl() {
    private val api = EventsApi()

    suspend fun getEventById(id: Long): Result<Event> {
        return try {
            val response = api.getEventById(id)
            val event = response.toEvent()
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTodayPopularEvents(): Result<EventData> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return try {
            val response = api.getPopularEvents(actualSince = today)
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