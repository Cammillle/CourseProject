package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alfabank.homework.courseproject.data.dto.lists.ListItem

@Composable
fun MapScreen(
    item: ListItem
) {
     Log.d("MapScreen","$item")
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text("Map screen")
    }
}