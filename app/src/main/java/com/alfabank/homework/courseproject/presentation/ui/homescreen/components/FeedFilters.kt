package com.alfabank.homework.courseproject.presentation.ui.homescreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alfabank.homework.courseproject.R

@Composable
fun FeedFilters(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
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
    val mapImages = mapOf(
        "Концерты" to R.drawable.concert,
        "Спектакли" to R.drawable.theater,
        "Экскурсии" to R.drawable.tour,
        "Ярмарки" to R.drawable.yarmarki,
        "Активный отдых" to R.drawable.recreation,
        "Выставки" to R.drawable.exhibition,
        "Фестивали" to R.drawable.festival
    )
    var selectedFilter by rememberSaveable { mutableStateOf(selectedCategory) }

    Column {
        LazyRow(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters1) { filter ->
                FilterChip(
                    text = filter,
                    imageId = mapImages[filter] ?: R.drawable.concert,
                    selected = selectedFilter == filter,
                    onClick = {
                        if (selectedFilter == filter) {
                            selectedFilter = "all"
                            onCategorySelected("all")
                        } else {
                            selectedFilter = filter
                            onCategorySelected(filter)
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
                    imageId = mapImages[filter] ?: R.drawable.concert,
                    selected = selectedFilter == filter,
                    onClick = {
                        if (selectedFilter == filter) {
                            selectedFilter = "all"
                            onCategorySelected("all")
                        } else {
                            selectedFilter = filter
                            onCategorySelected(filter)
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
    imageId: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.spacedBy(4.dp)
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(imageId),
                    contentDescription = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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
