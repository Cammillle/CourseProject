package com.alfabank.homework.courseproject.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.local.EventsRemoteMediator
import com.alfabank.homework.courseproject.data.local.toItem
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EventRepositoryImpl {
    private val api = EventsApi()
    private val database = DatabaseProvider.getDatabase()
    private val dao = database.eventDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getEventsPagingData(queryId: String, categories: List<String>): Flow<PagingData<Item>> {
        val pagingSourceFactory = { database.eventDao().getEventsByQueryId(queryId) }
        val remoteMediator = EventsRemoteMediator(
            db = database,
            api = api,
            queryId = queryId,
            categories = categories
        )

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toItem() }
        }
    }

    suspend fun clearQueryData(queryId: String) {
        database.eventDao().clearQueryData(queryId)
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
}

