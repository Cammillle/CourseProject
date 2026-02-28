package com.alfabank.homework.courseproject.presentation.ui.favouritescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.data.local.BookmarkRepositoryImpl
import com.alfabank.homework.courseproject.domain.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouriteViewModel : ViewModel() {
    private val _state = MutableStateFlow(ViewState(isLoading = true))
    val state = _state.asStateFlow()

    private val bookmarkRepository = BookmarkRepositoryImpl

    fun getBookmarks() = viewModelScope.launch {
        _state.update { state ->
            state.copy(isLoading = true)
        }
        bookmarkRepository.getBookmarks().collect {bookmarks->
            _state.update { state ->
                state.copy(items = bookmarks, isLoading = false)
            }
        }

    }

    fun addBookmark(item: Item) = viewModelScope.launch {
        bookmarkRepository.addBookmark(item)
    }

    fun removeBookmark(id: Long) = viewModelScope.launch {
        bookmarkRepository.deleteBookmark(id)
    }

    fun isBookmarked(id: Long): Boolean {
        var bookmarked = false
        viewModelScope.launch {
            bookmarked = bookmarkRepository.isBookmarked(id)
        }
        return bookmarked
    }

}

data class ViewState(
    val items: List<Item>? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String = "",
    val isWarning: Boolean = false,
)