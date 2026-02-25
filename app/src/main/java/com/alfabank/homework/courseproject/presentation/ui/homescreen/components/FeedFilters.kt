package com.alfabank.homework.courseproject.presentation.ui.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FeedFilters(
    onCategoryChange: (String) -> Unit,
    onCategoryClear: () -> Unit
) {
    val filters1 = listOf(
        "Концерты",
        "Спектакли",
        "Экскурсии",
        "Ярмарки",
        "Активный отдых"
    )
    val filters2 = listOf(
        "Выставки",
        "Фестивали"
    )
    var selectedFilter by rememberSaveable { mutableStateOf<String?>(null) }

    Column {
        LazyRow(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters1) { filter ->
                FilterChip(
                    text = filter,
                    selected = selectedFilter == filter,
                    onClick = {
                        if (selectedFilter == filter) {
                            selectedFilter = null
                            onCategoryClear()
                        } else {
                            selectedFilter = filter
                            onCategoryChange(filter)
                        }
                    }
                )
            }
        }
        LazyRow(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters2) { filter ->
                FilterChip(
                    text = filter,
                    selected = selectedFilter == filter,
                    onClick = {
                        if (selectedFilter == filter) {
                            selectedFilter = null
                            onCategoryClear()
                        } else {
                            selectedFilter = filter
                            onCategoryChange(filter)
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant,
            borderWidth = 1.dp,
            enabled = true,
            selected = selected
        ),
        shape = RoundedCornerShape(16.dp)
    )
}
