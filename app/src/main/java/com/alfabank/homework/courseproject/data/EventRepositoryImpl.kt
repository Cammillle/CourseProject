package com.alfabank.homework.courseproject.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.local.AllEventsRemoteMediator
import com.alfabank.homework.courseproject.data.local.CategoryEventsRemoteMediator
import com.alfabank.homework.courseproject.data.local.toItem
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

object EventRepositoryImpl {
    private val api = EventsApi()
    private val database = DatabaseProvider.getDatabase()
    private val dao = database.eventsDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getEventsWithCategory(category: String, city: String): Flow<PagingData<Item>> {

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
    fun getEventsWithoutCategory(city: String): Flow<PagingData<Item>> {
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

    fun getEventById(id: Long): Flow<Result<Item>> {
        return flow {
            val localEvent = dao.getEventById(id)
            if (localEvent != null) {
                emit(Result.success(localEvent.toItem()))
                return@flow
            }
        }.flowOn(Dispatchers.IO)
    }

    // Search (Local Only)
    fun searchEvent(query: String): Flow<List<Item>> {
        return dao.searchEvent(query).map { list ->
            list.map { entity -> entity.toItem() }
        }
    }

    //Favourite
    suspend fun toggleFavorite(id: Long, current: Boolean) {
        dao.updateFavorite(id, !current)
    }

    fun getFavouriteEvents(): Flow<List<Item>> = flow {
        val response = dao.getFavouriteEvents()
        response.collect {
            emit(it.map { it.toItem() })
        }
    }

}

