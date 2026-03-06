package com.alfabank.homework.courseproject.presentation.profilescreen

import androidx.lifecycle.ViewModel
import com.alfabank.homework.courseproject.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    suspend fun clearCache() {
        coroutineScope {
            listOf(
                launch { repository.clearAllEvents() },
                launch { repository.clearAllCrossRefs() },
                launch { repository.clearAllRemoteKeys() },
                launch { repository.clearAllListItems() },
                launch { repository.clearAllLists() },
                launch { repository.clearAllListCrossReffs() }
            )
        }
    }
}