package com.alfabank.homework.courseproject.presentation.ui.feedScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfabank.homework.courseproject.domain.model.Event

@Composable
fun FeedScreen(
    paddingValues: PaddingValues,
    events: List<Event> = emptyList(),
    nextDataIsLoading: Boolean,
    loadNextEvents: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        EventsList(
            events = events,
            nextDataIsLoading = nextDataIsLoading,
            loadNextEvents = loadNextEvents
        )
    }

}

@Composable
fun EventsList(
    events: List<Event> = emptyList(),
    nextDataIsLoading: Boolean,
    loadNextEvents: () -> Unit
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
            EventCard(event = it)
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