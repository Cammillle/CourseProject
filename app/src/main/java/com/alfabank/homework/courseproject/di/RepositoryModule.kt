package com.alfabank.homework.courseproject.di

import com.alfabank.homework.courseproject.data.local.repository.EventRepositoryImpl
import com.alfabank.homework.courseproject.data.local.repository.GuidRepositoryImpl
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