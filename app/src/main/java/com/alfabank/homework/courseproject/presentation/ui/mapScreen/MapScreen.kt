package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alfabank.homework.courseproject.data.dto.lists.ListItem
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.navigation.MapScreenArgs

@Suppress("NonSkippableComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mapArgs: MapScreenArgs,
    onBack: () -> Unit
) {
    Log.d("MapScreen", "map args $mapArgs")

    val viewModel: MapScreenViewModel = viewModel(
        factory = MapScreenViewModelFactory(mapArgs)
    )

    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val currentState = screenState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Гиды",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            YandexMapComponent(
                modifier = Modifier.fillMaxSize(),
                cameraPosition = currentState.cameraPosition,
                events = currentState.events,
                selectedEventId = currentState.selectedEvent?.id,
                onEventSelected = {
                    viewModel.selectEvent(it)
                }
            )

            AnimatedVisibility(
                visible = (currentState.selectedEvent != null) || (currentState.events.size == 1),
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                if (currentState.events.size == 1) {
                    val event = currentState.events[0]
                    MapEventCard(
                        event = event,
                        onClose = { viewModel.clearSelection() },
                        onBuyTickets = {
                            // openUrl(...)
                        }
                    )
                }
                currentState.selectedEvent?.let { event ->
                    MapEventCard(
                        event = event,
                        onClose = { viewModel.clearSelection() },
                        onBuyTickets = {
                            // openUrl(...)
                        }
                    )
                }
            }
        }
    }
}

