package com.alfabank.homework.courseproject.data.local

import com.alfabank.homework.courseproject.DatabaseProvider
import com.alfabank.homework.courseproject.data.EventsApi
import com.alfabank.homework.courseproject.domain.Item

object BookmarkRepositoryImpl {
    private val api = EventsApi()
    private val database = DatabaseProvider.getDatabase()
    private val bookmarkDao = database.bookmarksDao()
    private val eventDao = database.eventsDao()

    suspend fun addBookmark(item: Item) {
        bookmarkDao.addBookmark(item.toBookmarkEntity())
    }

    suspend fun deleteBookmark(id: Long) {
        bookmarkDao.deleteBookmark(id)
    }

    suspend fun isBookmarked(id: Long): Boolean = bookmarkDao.isBookmarked(id)

    suspend fun getBookmarks(): List<Item> {
        return bookmarkDao.getBookmarks().map { it.toItem() }
    }


}