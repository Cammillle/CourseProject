package com.alfabank.homework.courseproject

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfigManager: RemoteConfigManager
) {

    private val _apiKeyFlow = MutableStateFlow<String?>(null)
    val apiKeyFlow: StateFlow<String?> = _apiKeyFlow.asStateFlow()

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedPrefs: EncryptedSharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "map_sdk",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    suspend fun getApiKey(): String? = withContext(Dispatchers.IO) {
        _apiKeyFlow.value?.let { return@withContext it }

        getKeyFromEncryptedPrefs()?.let { cachedKey ->
            _apiKeyFlow.value = cachedKey
            return@withContext cachedKey
        }

        return@withContext fetchAndStoreKeyFromRemoteConfig()
    }

    private fun getKeyFromEncryptedPrefs(): String? {
        return encryptedPrefs.getString("map_sdk", null)
    }

    private suspend fun fetchAndStoreKeyFromRemoteConfig(): String? {
        return try {
            val apiKey = remoteConfigManager.fetchApiKey()
            if (!apiKey.isNullOrBlank()) {
                saveKey(apiKey)
                apiKey
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun saveKey(apiKey: String) {
        encryptedPrefs.edit().putString("map_sdk", apiKey).apply()
        _apiKeyFlow.value = apiKey
    }

    fun clearKey() {
        encryptedPrefs.edit().clear().apply()
        _apiKeyFlow.value = null
    }
}