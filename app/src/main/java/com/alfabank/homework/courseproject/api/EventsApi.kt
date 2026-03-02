package com.alfabank.homework.courseproject.api

import com.alfabank.homework.courseproject.data.dto.event.EventDTO
import com.alfabank.homework.courseproject.data.dto.event.ListOfEventsResponseDTO
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface EventsApi {

    @GET("events")
    suspend fun getPopularEventsByCategories(
        @Query("categories") categories: String,
        @Query("page") page: Int,
        @Query("actual_since") actualSince: String,
        @Query("order_by") orderBy: String = "-publication_date",
        @Query("expand") expand: List<String> = listOf("site_url,id,place,dates,location,description,images,title,age_restriction,price,categories"),
        @Query("location") location: String = "spb",
        @Query("text_format") textFormat: String = "text",
        @Query("page_size") pageSize: Int = 20,
        @Query("fields") fields: List<String> = listOf("site_url,id,tags,place,dates,location,description,images,title,age_restriction,price,categories,publication_date "),
    ): ListOfEventsResponseDTO

    @GET("events")
    suspend fun getEventsWithoutFilters(
        @Query("actual_since") actualSince: String,
        @Query("page") page: Int,
        @Query("order_by") orderBy: String = "-publication_date",
        @Query("expand") expand: List<String> = listOf("id,place,dates,location,description,images,title,age_restriction,price,categories"),
        @Query("location") location: String = "spb",
        @Query("text_format") textFormat: String = "text",
        @Query("page_size") pageSize: Int = 20,
        @Query("fields") fields: List<String> = listOf("id,tags,place,dates,location,description,images,title,age_restriction,price,categories,publication_date "),
    ): ListOfEventsResponseDTO

    @GET("events/{event_id}")
    suspend fun getEventById(
        @Path("event_id") eventId: Long,
        @Query("expand") expand: List<String> = listOf("images,place,location,dates"),
        @Query("text_format") textFormat: String = "text",
    ): EventDTO

    @GET
    suspend fun getNextEvents(
        @Url url: String
    ): ListOfEventsResponseDTO

}

fun EventsApi(
    baseUrl:String,
    json: Json,
    okHttpClient: OkHttpClient
): EventsApi {

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