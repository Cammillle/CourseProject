package com.alfabank.homework.courseproject.presentation.ui.feedScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.EventViewModel
import com.alfabank.homework.courseproject.presentation.ui.HomeState
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FeedFilters

@Composable
fun FeedScreen(
    paddingValues: PaddingValues,
    onEventClick: (Long) -> Unit,
) {
    val viewModel: EventViewModel = viewModel()
    val lazyPagingItems = viewModel.events.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        FeedFilters(
            onCategoryChange = { viewModel.observeCategory(it) },
            onCategoryClear = { viewModel.clearCategory() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        EventsList(
            items = lazyPagingItems,
            onClick = onEventClick
        )
    }
}

@Composable
fun EventsList(
    items: LazyPagingItems<Item>,
    onClick: (Long) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        items(
            count = items.itemCount,
            key = { index -> items[index]?.id ?: index }
        ) { index ->
            items[index]?.let { item ->
                EventCard(
                    event = item,
                    onClick = onClick
                )
            }
        }

        // Нижний лоадер
        if (items.loadState.append is LoadState.Loading) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    // Глобальный лоадер
    when (items.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            val error = (items.loadState.refresh as LoadState.Error).error
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error.localizedMessage ?: "Ошибка",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        else -> Unit
    }
}