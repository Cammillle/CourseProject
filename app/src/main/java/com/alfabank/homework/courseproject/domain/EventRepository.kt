package com.alfabank.homework.courseproject.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.alfabank.homework.courseproject.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    fun getEventsWithCategory(category: String, city: String): Flow<PagingData<Item>>

    fun getEventsWithoutCategory(city: String): Flow<PagingData<Item>>

    fun getEventById(id: Long): Flow<Result<Item>>

    // Search (Local Only)
    fun searchEvent(query: String): Flow<List<Item>>

    //Favourite
    suspend fun toggleFavorite(id: Long, current: Boolean)

    fun getFavouriteEvents(): Flow<List<Item>>

}