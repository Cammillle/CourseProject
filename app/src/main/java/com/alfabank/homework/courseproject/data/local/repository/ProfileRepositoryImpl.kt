package com.alfabank.homework.courseproject.data.local.repository

import com.alfabank.homework.courseproject.data.local.EventDatabase
import com.alfabank.homework.courseproject.domain.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val database: EventDatabase
) : ProfileRepository {

    override suspend fun clearAllEvents() {
        database.eventsDao().clearAllEvents()
    }

    override suspend fun clearAllCrossRefs() {
        database.eventsDao().clearCrossRefs()
    }

    override suspend fun clearAllRemoteKeys() {
        database.remoteKeysDao().clearAllRemoteKeys()
    }

    override suspend fun clearAllListItems() {
        database.listsDao().clearItems()
    }

    override suspend fun clearAllLists() {
        database.listsDao().clearAllLists()
    }

    override suspend fun clearAllListCrossReffs() {
        database.listsDao().clearListCrossReferences()
    }
}