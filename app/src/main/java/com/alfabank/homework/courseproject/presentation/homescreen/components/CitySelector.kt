package com.alfabank.homework.courseproject.presentation.homescreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CitySelector(
    cityName: String,
    onCityClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* открыть список городов */ }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onCityClick)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 4.dp),
                maxLines = 1
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Выбрать город",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewCitySelector() {
    CitySelector(
        cityName = "Санкт-Петербург",
        onCityClick = {}
    )
}