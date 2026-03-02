package com.alfabank.homework.courseproject.api

import com.alfabank.homework.courseproject.data.dto.lists.ListItemResponseDTO
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListsApi {

    @GET("lists/{item_id}")
    suspend fun getListItemsById(
        @Path("item_id") itemId: Long,
        @Query("text_format") textFormat: String = "text",
        //@Query("fields") fields: List<String> = listOf("-body_text"),
        @Query("expand") expand: List<String> = listOf("place","images")
    ): ListItemResponseDTO
}


fun ListsApi(
    baseUrl:String,
    json: Json,
    okHttpClient: OkHttpClient
): ListsApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            json
                .asConverterFactory("application/json".toMediaType())
        )
        .client(okHttpClient)
        .build()

    return retrofit.create(ListsApi::class.java)
}