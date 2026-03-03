package com.alfabank.homework.courseproject.presentation.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alfabank.homework.courseproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenCityDialog(
    currentCity: String,
    onDismiss: () -> Unit,
    onCitySelected: (String) -> Unit
) {
    val cities = stringArrayResource(R.array.cities)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Заголовок
            Text(
                text = "Выберите город",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 16.dp)
            )

            // Текущий выбранный город (выделенный)
            if (currentCity in cities) {
                CityItem(
                    cityName = currentCity,
                    isSelected = true,
                    onClick = onDismiss
                )
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.5.dp
                )
            }

            // Список всех городов
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(cities.filter { it != currentCity }.size) { index ->
                    val city = cities.filter { it != currentCity }[index]
                    CityItem(
                        cityName = city,
                        isSelected = false,
                        onClick = { onCitySelected(city) }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    FullScreenCityDialog(
        currentCity = "Санкт-Петербург",
        onDismiss = {},
        onCitySelected = {}
    )
}