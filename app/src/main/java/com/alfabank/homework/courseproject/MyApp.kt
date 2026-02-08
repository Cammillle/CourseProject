package com.alfabank.homework.courseproject

import android.app.Application
import android.content.Context
import com.alfabank.homework.courseproject.data.local.EventDatabase

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.init(this)
    }
}

object DatabaseProvider {
    private var _database: EventDatabase? = null

    fun init(context: Context) {
        if (_database == null) {
            _database = EventDatabase(context)
        }
    }

    fun getDatabase(): EventDatabase {
        return _database ?: throw IllegalStateException("Database not initialized")
    }
}