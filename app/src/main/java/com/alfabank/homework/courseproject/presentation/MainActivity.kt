package com.alfabank.homework.courseproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import com.alfabank.homework.courseproject.MainViewModel
import com.alfabank.homework.courseproject.presentation.ui.theme.CourseProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CourseProjectTheme {
                Surface {
                    MainScreen(viewModel.uiState, onRetry = viewModel::retry)
                }
            }
        }
    }

}