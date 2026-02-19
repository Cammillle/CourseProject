package com.alfabank.homework.courseproject.presentation.ui.guidscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidScreen(
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Гиды",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                )
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GuildFilters(
                onGuidChange = {}
            )
            YandexMapComponent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                cameraPosition = CameraPosition(
                    Point(59.9342802, 30.3350986),
                    11.0f,
                    0.0f,
                    0.0f),
                events = emptyList(),
                selectedEventId = 1
            ) { }

        }


    }

}