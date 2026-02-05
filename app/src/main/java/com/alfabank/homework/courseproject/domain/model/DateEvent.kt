package com.alfabank.homework.courseproject.domain.model

data class DateEvent(
    //val end: Int?,
    val endDate: String?,
    val endTime: String?,
    val isContinuous: Boolean?,
    val isEndless: Boolean?,
    val isStartless: Boolean?,
    //@SerialName("schedules")
    //val schedules: List<Any?>?,
    //val start: Int?,
    val startDate: String?,
    val startTime: String?,
    val usePlaceSchedule: Boolean?
)