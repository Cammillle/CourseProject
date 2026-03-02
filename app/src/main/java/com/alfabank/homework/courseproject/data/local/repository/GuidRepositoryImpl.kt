package com.alfabank.homework.courseproject.data.local.repository

import com.alfabank.homework.courseproject.api.ListsApi
import com.alfabank.homework.courseproject.data.dto.lists.ListItemResponseDTO
import com.alfabank.homework.courseproject.data.dto.lists.toListItemEntity
import com.alfabank.homework.courseproject.data.local.EventDatabase
import com.alfabank.homework.courseproject.data.local.dbo.ListItemCrossEntity
import com.alfabank.homework.courseproject.data.local.dbo.toItemEntity
import com.alfabank.homework.courseproject.data.local.dbo.toListWithItems
import com.alfabank.homework.courseproject.domain.GuidRepository
import com.alfabank.homework.courseproject.domain.model.ListWithItems
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GuidRepositoryImpl @Inject constructor(
    private val api: ListsApi,
    private val database: EventDatabase
) : GuidRepository {

    override suspend fun getListsById(id: Long): Result<ListWithItems> {
        val cached = database.listsDao().getListWithItems(id)
        if (cached != null) {
            return Result.success(cached.toListWithItems())
        }
        return try {
            val response = api.getListItemsById(id)
            cacheListResponse(response)
            val cached = database.listsDao().getListWithItems(id)
            Result.success((cached!!.toListWithItems()))
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun cacheListResponse(dto: ListItemResponseDTO) {
        val listEntity = dto.toListItemEntity()
        val itemEntities = dto.items?.map { it.toItemEntity() } ?: emptyList()
        val crossEntities =
            dto.items?.map { ListItemCrossEntity(listEntity.id, it.id) } ?: emptyList()

        database.listsDao().insertListResponse(listEntity)
        database.listsDao().insertItems(itemEntities)
        database.listsDao().insertCrossReferences(crossEntities)
    }


}