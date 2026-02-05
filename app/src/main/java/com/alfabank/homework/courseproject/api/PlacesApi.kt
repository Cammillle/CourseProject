package com.alfabank.homework.courseproject.api

import com.alfabank.homework.courseproject.data.EventsApi
import com.alfabank.homework.courseproject.data.places.PlacesResponseDTO
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("places")
    suspend fun getPopularPlaces(
        @Query("actual_since") actualSince: String,
        @Query("order_by") orderBy: String = "-favorites_count,-publication_date",
        @Query("expand") expand: List<String> = listOf("id,title,address,images,description,coords,categories,tags,timetable"),
        @Query("location") location: String = "spb",
        @Query("text_format") textFormat: String = "text",
        @Query("fields") fields: List<String> = listOf("id,title,address,images,description,coords,categories,tags,timetable"),
    ): PlacesResponseDTO

}

fun PlacesApi(): PlacesApi {
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

    return retrofit.create(PlacesApi::class.java)
}