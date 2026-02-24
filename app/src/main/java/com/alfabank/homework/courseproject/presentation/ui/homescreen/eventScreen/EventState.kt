package com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen

import com.alfabank.homework.courseproject.domain.Item

data class EventState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var selectedEvent: Item? = null
)