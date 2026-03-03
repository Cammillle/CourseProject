package com.alfabank.homework.courseproject

import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor() {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("map_sdk" to ""))
    }

    suspend fun fetchApiKey(): String? {
        return try {
            remoteConfig.fetchAndActivate().await()
            val key = remoteConfig.getString("map_sdk")
            if (key.isNotBlank()) key else null
        } catch (e: Exception) {
            remoteConfig.getString("map_sdk").takeIf { it.isNotBlank() }
        }
    }
}