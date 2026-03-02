package com.alfabank.homework.courseproject.domain

import com.alfabank.homework.courseproject.data.dto.lists.ListItemResponseDTO
import com.alfabank.homework.courseproject.domain.model.ListWithItems

interface GuidRepository {
    suspend fun getListsById(id: Long): Result<ListWithItems>

    suspend fun cacheListResponse(dto: ListItemResponseDTO)
}