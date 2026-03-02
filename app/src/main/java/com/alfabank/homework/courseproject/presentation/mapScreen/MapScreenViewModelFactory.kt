package com.alfabank.homework.courseproject.presentation.mapScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.alfabank.homework.courseproject.navigation.MapScreenArgs
import com.alfabank.homework.courseproject.navigation.Screen

class MapScreenViewModelFactory(
    private val args: MapScreenArgs?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return MapScreenViewModel(
            SavedStateHandle(mapOf(Screen.KEY_MAP_ARGS to args))
        ) as T
    }
}