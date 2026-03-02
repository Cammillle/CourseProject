package com.alfabank.homework.courseproject.domain

import com.alfabank.homework.courseproject.domain.model.EventData

interface EventRepository {

    suspend fun getAllEvents(pageSize: Int): Result<EventData>

    suspend fun getEventsByCategory(pageSize: Int, category: String): Result<EventData>


}