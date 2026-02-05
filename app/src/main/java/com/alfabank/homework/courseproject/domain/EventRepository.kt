package com.alfabank.homework.courseproject.domain

interface EventRepository {

    suspend fun getAllEvents(pageSize: Int): Result<EventData>

    suspend fun getEventsByCategory(pageSize: Int, category: String): Result<EventData>


}