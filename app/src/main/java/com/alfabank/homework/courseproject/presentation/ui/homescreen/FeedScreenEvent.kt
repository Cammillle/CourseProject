package com.alfabank.homework.courseproject.presentation.ui.homescreen

sealed class FeedScreenEvent {
    object onLoadNextData : FeedScreenEvent()
    data class onSearchQueryChange(val query: String) : FeedScreenEvent()
    data class onCategoryChange(val category: String) : FeedScreenEvent()
}