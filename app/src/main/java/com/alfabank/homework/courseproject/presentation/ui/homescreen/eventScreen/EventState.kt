package com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen

import com.alfabank.homework.courseproject.domain.model.Event

data class EventState(
    var event: Event? = null,
    var isLoading: Boolean = false,
    var error: String? = null,
)