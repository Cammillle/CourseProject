package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfabank.homework.courseproject.navigation.Screen
import kotlinx.coroutines.launch

class MapScreenViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {



    init {
        viewModelScope.launch {
            val listId = savedStateHandle.get<Long>(Screen.KEY_LIST_ITEM_ID) ?: return@launch
            Log.d("MapScreen","$listId")



            //getEventById(id = eventId)
        }
    }



}