package com.alfabank.homework.courseproject.presentation.homescreen.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.presentation.utils.getSlug

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(
    navigateOnFilterScreen: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onRefreshSearch: () -> Unit,
    onCitySelected: (String) -> Unit
) {
    var showCityDialog by remember { mutableStateOf(false) }

    var selectedCity by remember { mutableStateOf("Санкт-Петербург") }

    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        title = {
            Log.d("ViewModel", "is search bar is $active")
            if (active) {
                BasicTextField(
                    value = searchQuery,
                    singleLine = true,
                    textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    onValueChange = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                        active = (it.isNotBlank())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { },
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Box(Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Поиск...",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            } else {
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
            }
        },
        actions = {
            if (active) {
                //Кнопка закрытия поиска
                IconButton(onClick = {
                    active = false
                    searchQuery = ""
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть поиск")
                }
            } else {
                IconButton(onClick = { active = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Поиск")
                }
                IconButton(onClick = { navigateOnFilterScreen() }) {
                    Icon(
                        painterResource(R.drawable.outline_filter_list_24),
                        contentDescription = "Фильтры"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )

    LaunchedEffect(active) {
        if (active) {
            focusRequester.requestFocus()
        } else {
            onRefreshSearch()
        }
    }

    if (showCityDialog) {
        FullScreenCityDialog(
            currentCity = selectedCity,
            onDismiss = {
                showCityDialog = false
            },
            onCitySelected = { city ->
                selectedCity = city
                showCityDialog = false
                Log.d("city", "$city")
                val slug = city.getSlug()
                onCitySelected(slug)
                Log.d("city", city.getSlug())
            }
        )
    }
}
