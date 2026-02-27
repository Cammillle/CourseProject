package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alfabank.homework.courseproject.navigation.MapScreenArgs
import com.alfabank.homework.courseproject.presentation.ui.guidscreen.composable.ListItemCard
import com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen.firstUppercase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidScreen(
    onMapNavigate: (MapScreenArgs) -> Unit
) {
    val viewModel: GuidViewModel = viewModel()
    val guidState = viewModel.guidScreenState.collectAsStateWithLifecycle()
    val lists = guidState.value.lists

    val currentState = guidState.value

    if(currentState.error == null){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Гиды и подборки",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = lists) { item ->
                    ListItemCard(
                        title = item.title!!.firstUppercase(),
                        description = item.description!!.firstUppercase(),
                        imageUrls = item.images ?: emptyList(),
                        onMapNavigate = {
                            onMapNavigate(MapScreenArgs.ListData(item))
                        },
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (currentState.isLoading) {
            CircularProgressIndicator()
        } else if (currentState.error != null) {
            Text(
                text = currentState.error.toString(),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}