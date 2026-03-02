package com.alfabank.homework.courseproject.presentation.homescreen.eventScreen

import com.alfabank.homework.courseproject.domain.model.Item

data class EventState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var selectedEvent: Item? = null
)