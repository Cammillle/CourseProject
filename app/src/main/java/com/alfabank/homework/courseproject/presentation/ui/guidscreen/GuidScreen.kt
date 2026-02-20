package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alfabank.homework.courseproject.presentation.ui.guidscreen.composable.ListItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidScreen(
) {
    val viewModel: GuidViewModel = viewModel()
    val guidState = viewModel.guidScreenState.collectAsStateWithLifecycle()
    val lists = guidState.value.lists

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Гиды",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                )
            })
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
                    title = item.title ?: "",
                    description = item.description ?: "",
                    imageUrls = item.images ?: emptyList(),
                    onClick = {}
                )
            }
        }
    }
}