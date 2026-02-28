package com.alfabank.homework.courseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmarkEntity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Long)

    @Query("SELECT EXISTS(SELECT * FROM bookmarks WHERE id = :id)")
    suspend fun isBookmarked(id: Long): Boolean

    @Query("SELECT * FROM bookmarks ORDER BY publicationDate DESC")
    suspend fun getBookmarks(): List<BookmarkEntity>
}