package com.alfabank.homework.courseproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfabank.homework.courseproject.MainUiState
import com.alfabank.homework.courseproject.MainViewModel
import com.alfabank.homework.courseproject.presentation.ui.theme.CourseProjectTheme
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourseProjectTheme {
                Surface {
                    val viewModel: MainViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                    when (val currentState = uiState.value) {
                        is MainUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is MainUiState.Success -> {
                            val apiKey = currentState.apiKey
                            val context = LocalContext.current

                            LaunchedEffect(apiKey) {
                                MapKitFactory.setApiKey(apiKey)
                                MapKitFactory.initialize(context)
                            }

                        }

                        is MainUiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(onClick = viewModel::retry) {
                                    Text("Ошибка: ${(uiState as MainUiState.Error).message}. Повторить")
                                }
                            }
                        }
                    }
                    MainScreen()
                }
            }
        }
    }

}