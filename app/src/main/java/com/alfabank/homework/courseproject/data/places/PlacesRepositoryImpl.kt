package com.alfabank.homework.courseproject.data.places

import com.alfabank.homework.courseproject.api.PlacesApi

import com.alfabank.homework.courseproject.data.toPlace
import com.alfabank.homework.courseproject.domain.PlaceData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlacesRepositoryImpl {

    private val api = PlacesApi()

    suspend fun getTodayPopularEvents(): Result<PlaceData> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return try {
            val response = api.getPopularPlaces(actualSince = today)
            val places = response.places?.map { it.toPlace() } ?: emptyList()
            val nextPage = response.next
            Result.success(PlaceData(places = places, nextUrl = nextPage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}