package com.alfabank.homework.courseproject.presentation.ui.mapscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FilterChip

@Composable
fun GuildFilters(
    onGuidChange: (String) -> Unit
) {

    val guids = listOf(
        "Все сразу", "Концерты", "Спектакли",
        "Экскурсии",
        "Ярмарки",
        "Активный отдых"
    )


    var selectedFilter by remember { mutableStateOf<String?>(guids[0]) }

    Column {
        LazyRow(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(guids) { guid ->
                FilterChip(
                    text = guid,
                    selected = selectedFilter == guid,
                    onClick = {
                        selectedFilter = guid
                        onGuidChange(guid)
                    }
                )
            }
        }
    }


}
