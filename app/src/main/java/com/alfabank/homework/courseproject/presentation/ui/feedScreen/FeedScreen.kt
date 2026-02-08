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
import com.alfabank.homework.courseproject.domain.model.Event
import com.alfabank.homework.courseproject.presentation.ui.HomeState
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FeedFilters

@Suppress("NonSkippableComposable")
@Composable
fun FeedScreen(
    paddingValues: PaddingValues,
    state: HomeState,
    loadNextEvents: () -> Unit,
    onEventClick: (Long) -> Unit,
    onCategoryChange: (String) -> Unit,
    onCategoryClear: () -> Unit
) {
    if (state.error == null) {
        val events = state.events
        val nextDataIsLoading = state.nextDataIsLoading
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FeedFilters(
                onCategoryChange = onCategoryChange,
                onCategoryClear = onCategoryClear
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!state.isLoading) {
                EventsList(
                    events = events,
                    nextDataIsLoading = nextDataIsLoading,
                    loadNextEvents = loadNextEvents,
                    onClick = onEventClick
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text(
                text = state.error.toString(),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun EventsList(
    events: List<Event> = emptyList(),
    nextDataIsLoading: Boolean,
    loadNextEvents: () -> Unit,
    onClick: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            totalItemsCount > 0 && lastVisibleItemIndex >= (totalItemsCount - 3) && !nextDataIsLoading
        }
    }

    LaunchedEffect(loadMore.value) {
        if (loadMore.value) {
            loadNextEvents()
        }
    }

    LazyColumn(
        state = listState, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        items(items = events, key = { it.id }) {
            EventCard(event = it, onClick = onClick)
        }
        if (nextDataIsLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}