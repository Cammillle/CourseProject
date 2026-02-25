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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.EventViewModel
import com.alfabank.homework.courseproject.presentation.ui.HomeState
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FeedFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    paddingValues: PaddingValues,
    onEventClick: (Long) -> Unit,
) {
    val viewModel: EventViewModel = viewModel()
    val selectedCategories by viewModel.selectedCategories.collectAsStateWithLifecycle()
    val events = viewModel.eventsPagingData.collectAsLazyPagingItems()
    val isRefreshing = events.loadState.refresh is LoadState.Loading

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

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(
                    count = events.itemCount,
                    key = { index -> events[index]?.id ?: index }
                ) { index ->
                    events[index]?.let { event ->
                        EventCard(
                            event = event,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }

                events.apply {
                    when (val refreshState = loadState.refresh) {
                        is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    message = "Ошибка загрузки: ${refreshState.error.message}",
                                    onRetry = { retry() }
                                )
                            }
                        }

                        else -> {}
                    }

                    when (val appendState = loadState.append) {
                        is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    message = "Ошибка пагинации",
                                    onRetry = { retry() }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Повторить")
            }
        }
    }
}