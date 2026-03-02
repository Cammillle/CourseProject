package com.alfabank.homework.courseproject.presentation.mapScreen

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfabank.homework.courseproject.presentation.utils.openUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit
) {

    val viewModel: MapScreenViewModel = hiltViewModel()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val currentState = screenState.value

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults
                    .topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
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
                visible = (currentState.listItem != null),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                val listItem = currentState.listItem
                ListItemCard(listItem)
            }


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
                            context.openUrl(event.itemUrl) { error ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(error)
                                }
                            }
                        }
                    )
                }
                currentState.selectedEvent?.let { event ->
                    MapEventCard(
                        event = event,
                        onClose = { viewModel.clearSelection() },
                        onBuyTickets = {
                            context.openUrl(event.itemUrl) { error ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(error)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

