package com.alfabank.homework.courseproject.data.local.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alfabank.homework.courseproject.api.EventsApi
import com.alfabank.homework.courseproject.data.local.paging.AllEventsRemoteMediator
import com.alfabank.homework.courseproject.data.local.paging.CategoryEventsRemoteMediator
import com.alfabank.homework.courseproject.data.local.EventDatabase
import com.alfabank.homework.courseproject.data.local.toItem
import com.alfabank.homework.courseproject.domain.EventRepository
import com.alfabank.homework.courseproject.domain.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventsApi,
    private val database: EventDatabase
) : EventRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getEventsWithCategory(category: String, city: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
            ),
            remoteMediator = CategoryEventsRemoteMediator(
                category = category,
                api = api,
                db = database,
                city = city
            ),
            pagingSourceFactory = {
                Log.d("CAtegory paging", " $category $city")
                val paging = database.eventsDao().pagingSourceByCategory(category, city)
                Log.d("CAtegory paging", " $paging")
                database.eventsDao().pagingSourceByCategory(category, city)
            }
        ).flow.map {
            it.map { it.toItem() }
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getEventsWithoutCategory(city: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
            ),
            remoteMediator = AllEventsRemoteMediator(
                api = api,
                db = database,
                city = city
            ),
            pagingSourceFactory = {
                database.eventsDao().pagingSourceAll(city)
            }
        ).flow.map {
            it.map { it.toItem() }
        }
    }

    override fun getEventById(id: Long): Flow<Result<Item>> {
        return flow {
            val localEvent = database.eventsDao().getEventById(id)
            if (localEvent != null) {
                emit(Result.success(localEvent.toItem()))
                return@flow
            }
        }.flowOn(Dispatchers.IO)
    }

    // Search (Local Only)
    override fun searchEvent(query: String): Flow<List<Item>> {
        return database.eventsDao().searchEvent(query).map { list ->
            list.map { entity -> entity.toItem() }
        }
    }

    //Favourite
    override suspend fun toggleFavorite(id: Long, current: Boolean) {
        database.eventsDao().updateFavorite(id, !current)
    }

    override fun getFavouriteEvents(): Flow<List<Item>> = flow {
        val response = database.eventsDao().getFavouriteEvents()
        response.collect {
            emit(it.map { it.toItem() })
        }
    }

}

