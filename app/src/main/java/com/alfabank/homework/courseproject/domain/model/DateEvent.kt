package com.alfabank.homework.courseproject.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DateEvent(
    val endDate: String?,
    val endTime: String?,
    val isContinuous: Boolean?,
    val isEndless: Boolean?,
    val isStartless: Boolean?,
    val startDate: String?,
    val startTime: String?,
    val usePlaceSchedule: Boolean?
)