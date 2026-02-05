package com.alfabank.homework.courseproject.domain

import com.alfabank.homework.courseproject.domain.model.Event

data class EventData(
    val events: List<Event>,
    val nextUrl: String?
)