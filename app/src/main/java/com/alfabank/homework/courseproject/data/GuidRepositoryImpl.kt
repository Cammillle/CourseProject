package com.alfabank.homework.courseproject.data

import coil.network.HttpException
import com.alfabank.homework.courseproject.api.ListsApi
import com.alfabank.homework.courseproject.data.dto.lists.ListItemResponse
import com.alfabank.homework.courseproject.data.dto.lists.toListItemResponse
import okio.IOException

class GuidRepositoryImpl {
    private val api = ListsApi()

    suspend fun getListsById(id: Int): Result<ListItemResponse> {
        val response = try {
            api.getListItemsById(id)
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.failure(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            return Result.failure(e)
        }
        return Result.success(response.toListItemResponse())
    }
}