package com.alfabank.homework.courseproject.presentation.ui.homescreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.alfabank.homework.courseproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(
    navigateOnFilterScreen: () -> Unit
) {
    var showCityDialog by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Санкт-Петербург") }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ЛЕНТА",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                CitySelector(
                    cityName = selectedCity,
                    onCityClick = { showCityDialog = true }
                )
            }
        },
        actions = {
            IconButton(onClick = { /* поиск */ }) {
                Icon(Icons.Default.Search, contentDescription = "Поиск")
            }
            IconButton(onClick = { navigateOnFilterScreen() }) {
                Icon(
                    painterResource(R.drawable.outline_filter_list_24),
                    contentDescription = "Фильтры"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )

    if (showCityDialog) {
        FullScreenCityDialog(
            currentCity = selectedCity,
            onDismiss = { showCityDialog = false },
            onCitySelected = { city ->
                selectedCity = city
                showCityDialog = false
            }
        )
    }
}

@Composable
@Preview
private fun Preview() {
    FeedTopBar(
        navigateOnFilterScreen = {}
    )
}
