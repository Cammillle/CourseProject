package com.alfabank.homework.courseproject

import android.content.Context
import com.alfabank.homework.courseproject.api.EventsApi
import com.alfabank.homework.courseproject.api.ListsApi
import com.alfabank.homework.courseproject.data.local.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEventsApi(): EventsApi {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val baseUrl = "https://kudago.com/public-api/v1.4/"

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
            // Custom serializers would be added here if needed
            // serializersModule = SerializersModule {
            //     contextual(Date::class, DateSerializer)
            // }
        }
        return EventsApi(
            baseUrl = baseUrl,
            json = json,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideListsApi(): ListsApi {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val baseUrl = "https://kudago.com/public-api/v1.4/"

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
            // Custom serializers would be added here if needed
            // serializersModule = SerializersModule {
            //     contextual(Date::class, DateSerializer)
            // }
        }
        return ListsApi(
            baseUrl = baseUrl,
            json = json,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EventDatabase {
        return EventDatabase(context)
    }

}