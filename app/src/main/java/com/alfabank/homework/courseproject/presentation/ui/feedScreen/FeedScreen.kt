package com.alfabank.homework.courseproject.presentation.ui.feedScreen

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FeedFilters

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
            LazyColumn(
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
            Log.d("Paging","currentPagingFlow ${currentPagingFlow}")

            Log.d("Paging","currentPagingFlow itemCount ${currentPagingFlow.itemCount}")
            Log.d("Paging","currentPagingFlow load state refresh ${currentPagingFlow.loadState.refresh}")

//            // ---------- Empty State ----------
//            if (currentPagingFlow.itemCount == 0 &&
//                currentPagingFlow.loadState.refresh is LoadState.NotLoading
//            ) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("No events found")
//                }
//            }

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