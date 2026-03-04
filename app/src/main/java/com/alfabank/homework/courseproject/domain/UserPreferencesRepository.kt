package com.alfabank.homework.courseproject.domain

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //создается один экземляр при первом обращении из-за делегата preferencesDataStore
    val dataStore = context.dataStore
    val selectedCity: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CITY_PREFERENCES_KEY] ?: "spb"
        }

    suspend fun saveCity(newCity: String) {
        dataStore.edit { preferences ->
            preferences[CITY_PREFERENCES_KEY] = newCity
        }
    }


    companion object {
        private val CITY_PREFERENCES_KEY = stringPreferencesKey("selected_city")
    }
}