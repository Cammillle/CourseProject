package com.alfabank.homework.courseproject.domain

interface ProfileRepository {

    suspend fun clearAllEvents()
    suspend fun clearAllCrossRefs()
    suspend fun clearAllRemoteKeys()

}