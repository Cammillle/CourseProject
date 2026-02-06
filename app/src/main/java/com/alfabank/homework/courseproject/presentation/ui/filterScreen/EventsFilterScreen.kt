package com.alfabank.homework.courseproject.presentation.ui.filterScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsFilterScreen(
    onBackClick: () -> Unit,
    onClearAll: () -> Unit
) {

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Фильтр",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            },
            actions = {
                TextButton(onClick = onClearAll) {
                    Text(
                        text = "Очистить",
                        fontSize = 14.sp
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HorizontalDivider()
            }
            item {
                AgeRatingFilter()
            }
            item {
                CategoriesFilter()
            }
        }

    }

}

@Composable
@Preview
private fun Preview() {
    EventsFilterScreen(
        onBackClick = {},
        onClearAll = {}
    )
}
