package com.alfabank.homework.courseproject.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alfabank.homework.courseproject.domain.model.Event
import com.alfabank.homework.courseproject.presentation.ui.homescreen.EventCard
import com.alfabank.homework.courseproject.presentation.ui.homescreen.EventViewModel
import com.alfabank.homework.courseproject.presentation.ui.homescreen.HomeScreen
import com.alfabank.homework.courseproject.presentation.ui.theme.CourseProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            CourseProjectTheme {
                HomeScreen()
            }
        }
    }
}


