package com.alfabank.homework.courseproject.presentation.ui.feedScreen

sealed class EventGroup {
    object TodayPopular : EventGroup()
    data class Category(val category: String) : EventGroup()
    data class Search(val query: String) : EventGroup()
}