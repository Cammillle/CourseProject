package com.alfabank.homework.courseproject.data

import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.api.ListsApi
import com.alfabank.homework.courseproject.data.dto.lists.ListItemResponseDTO
import com.alfabank.homework.courseproject.data.dto.lists.toListItemEntity
import com.alfabank.homework.courseproject.data.local.ListItemCrossEntity
import com.alfabank.homework.courseproject.data.local.toItemEntity
import com.alfabank.homework.courseproject.data.local.toListWithItems
import com.alfabank.homework.courseproject.domain.model.ListWithItems
import okio.IOException
import retrofit2.HttpException

class GuidRepositoryImpl {
    private val api = ListsApi()

    private val database = DatabaseProvider.getDatabase()
    private val dao = database.listsDao()

    suspend fun getListsById(id: Long): Result<ListWithItems> {
        val cached = dao.getListWithItems(id)
        if (cached != null) {
            return Result.success(cached.toListWithItems())
        }
        return try {
            val response = api.getListItemsById(id)
            cacheListResponse(response)
            val cached = dao.getListWithItems(id)
            Result.success((cached!!.toListWithItems()))
        }catch (e: IOException){
            e.printStackTrace()
            Result.failure(e)
        }catch (e: HttpException) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private suspend fun cacheListResponse(dto: ListItemResponseDTO) {
        val listEntity = dto.toListItemEntity()
        val itemEntities = dto.items?.map { it.toItemEntity() } ?: emptyList()
        val crossEntities = dto.items?.map { ListItemCrossEntity(listEntity.id, it.id) } ?: emptyList()

        dao.insertListResponse(listEntity)
        dao.insertItems(itemEntities)
        dao.insertCrossReferences(crossEntities)
    }


}