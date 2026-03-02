package com.alfabank.homework.courseproject

import com.alfabank.homework.courseproject.data.EventRepositoryImpl
import com.alfabank.homework.courseproject.data.GuidRepositoryImpl
import com.alfabank.homework.courseproject.domain.EventRepository
import com.alfabank.homework.courseproject.domain.GuidRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository

    @Binds
    abstract fun bindGuidRepository(
        impl: GuidRepositoryImpl
    ): GuidRepository
}