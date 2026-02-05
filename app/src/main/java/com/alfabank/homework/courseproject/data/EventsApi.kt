package com.alfabank.homework.courseproject.data

import com.alfabank.homework.courseproject.data.dto.ListOfEventsResponseDTO
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface EventsApi {

    //id,place,dates,location,description,images,title,age_restriction,price,categories
    @GET("events")
    suspend fun getAllEvents(
        @Query("expand") expand: List<String> = listOf("id,place,dates,location,description,images,title,age_restriction,price,categories"),
        @Query("location") location: String = "spb",
        @Query("page_size") pageSize: Int = 20,
        @Query("fields") fields: List<String> = listOf("id,place,dates,location,description,images,title,age_restriction,price,categories"),
    ): ListOfEventsResponseDTO

    @GET("events")
    suspend fun getEventsByCategories(
        @Query("expand") expand: List<String> = listOf("id,place,dates,location,description,images,title,age_restriction,price,categories"),
        @Query("location") location: String = "spb",
        @Query("page_size") pageSize: Int = 20,
        @Query("categories") categories: String,
        @Query("fields") fields: List<String> = listOf("id,place,dates,location,description,images,title,age_restriction,price,categories"),
    ): ListOfEventsResponseDTO

    @GET
    suspend fun getNextEvents(
        @Url url: String
    ): ListOfEventsResponseDTO

}

fun EventsApi(): EventsApi {
    val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val baseUrl = "https://kudago.com/public-api/v1.4/"

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        // Custom serializers would be added here if needed
        // serializersModule = SerializersModule {
        //     contextual(Date::class, DateSerializer)
        // }
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            json
                .asConverterFactory("application/json".toMediaType())
        )
        .client(okHttpClient)
        .build()

    return retrofit.create(EventsApi::class.java)
}