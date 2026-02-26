package com.alfabank.homework.courseproject.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.local.EventEntity
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
    private val dao = database.eventsDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getEvents(category: String?): Flow<PagingData<EventEntity>> {

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = EventsRemoteMediator(
                category = category,
                api = api,
                db = database
            ),
            pagingSourceFactory = {
                if (category == null) {
                    database.eventsDao().pagingSourceAll()
                } else {
                    database.eventsDao().pagingSourceByCategory(category)
                }
            }
        ).flow
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
}

