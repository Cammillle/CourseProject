package com.alfabank.homework.courseproject.presentation.homescreen.feedScreen

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.alfabank.homework.courseproject.domain.model.Item
import com.alfabank.homework.courseproject.presentation.homescreen.components.FeedFilters
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    paddingValues: PaddingValues,
    onEventClick: (Long) -> Unit,
    onBookmarkClick: (Item) -> Unit,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    isRefreshing: Boolean,
    currentPagingFlow: LazyPagingItems<Item>,
    searchList: List<Item> = emptyList()
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 3
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FeedFilters(
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                onSelectCategory(category)
            }
        )


        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { currentPagingFlow.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (searchList.isNotEmpty()) {
                        items(items = searchList, key = { it.id }) { event ->
                            EventCard(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                onBookmarkClick = onBookmarkClick,
                            )
                        }
                    }
                    items(
                        count = currentPagingFlow.itemCount,
                        key = currentPagingFlow.itemKey { it.id }
                    ) { index ->
                        val event = currentPagingFlow[index]
                        event?.let { event ->
                            EventCard(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                onBookmarkClick = onBookmarkClick,
                            )
                        }
                    }
                    // ---------- Append Loader ----------
                    when (currentPagingFlow.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                RetryItem {
                                    currentPagingFlow.retry()
                                }
                            }
                        }

                        else -> Unit
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = showScrollToTopButton,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null
                        )
                    }
                }

                Log.d("Paging", "currentPagingFlow ${currentPagingFlow}")

                Log.d("Paging", "currentPagingFlow itemCount ${currentPagingFlow.itemCount}")
                Log.d(
                    "Paging",
                    "currentPagingFlow load state refresh ${currentPagingFlow.loadState.refresh}"
                )

                // ---------- Empty State ----------
                if (currentPagingFlow.itemCount == 0 &&
                    currentPagingFlow.loadState.refresh is LoadState.NotLoading
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No events found")
                    }
                }

                // ---------- First Load Error ----------
                if (currentPagingFlow.loadState.refresh is LoadState.Error) {
                    val error = currentPagingFlow.loadState.refresh as LoadState.Error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        RetryItem(
                            message = error.error.message,
                            onRetry = { currentPagingFlow.retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RetryItem(
    message: String? = "Something went wrong",
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message.orEmpty())
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
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