package com.alfabank.homework.courseproject.presentation.ui.filterScreen.dateFilter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterWithPickers() {
    var expanded by remember { mutableStateOf(true) }

    var dateFrom by remember { mutableStateOf<LocalDate?>(null) }
    var dateTo by remember { mutableStateOf<LocalDate?>(null) }
    var selectedQuickFilter by remember { mutableStateOf<String?>(null) }

    // Состояния для DatePicker
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Дата",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Свернуть" else "Развернуть",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DatePickerCard(
                            modifier = Modifier.weight(1f),
                            title = "От",
                            date = dateFrom,
                            onClick = { showFromDatePicker = true }
                        )

                        DatePickerCard(
                            modifier = Modifier.weight(1f),
                            title = "До",
                            date = dateTo,
                            onClick = { showToDatePicker = true }
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "Любое время",
                                "Сегодня",
                                "Завтра"
                            ).forEach { filter ->
                                QuickFilterChipSimple(
                                    text = filter,
                                    selected = selectedQuickFilter == filter,
                                    onClick = {
                                        if (selectedQuickFilter == filter) {
                                            selectedQuickFilter = null
                                            dateFrom = null
                                            dateTo = null
                                        } else {
                                            selectedQuickFilter = filter
                                            val dates = when (filter) {
                                                "Сегодня" -> {
                                                    val today = LocalDate.now()
                                                    Pair(today, today)
                                                }

                                                "Завтра" -> {
                                                    val tomorrow = LocalDate.now().plusDays(1)
                                                    Pair(tomorrow, tomorrow)
                                                }

                                                else -> null
                                            }
                                            dates?.let {
                                                dateFrom = it.first
                                                dateTo = it.second
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "Эта неделя", "Выходные"
                            ).forEach { filter ->
                                QuickFilterChipSimple(
                                    text = filter,
                                    selected = selectedQuickFilter == filter,
                                    onClick = {
                                        if (selectedQuickFilter == filter) {
                                            selectedQuickFilter = null
                                            dateFrom = null
                                            dateTo = null
                                        } else {
                                            selectedQuickFilter = filter
                                            val dates = when (filter) {
                                                "Эта неделя" -> {
                                                    val today = LocalDate.now()
                                                    val monday = today.with(
                                                        TemporalAdjusters.previousOrSame(
                                                            DayOfWeek.MONDAY
                                                        )
                                                    )
                                                    val sunday = today.with(
                                                        TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)
                                                    )
                                                    Pair(monday, sunday)
                                                }

                                                "Выходные" -> {
                                                    val today = LocalDate.now()
                                                    val saturday = today.with(
                                                        TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)
                                                    )
                                                    val sunday = today.with(
                                                        TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)
                                                    )
                                                    Pair(saturday, sunday)
                                                }

                                                else -> null
                                            }
                                            dates?.let {
                                                dateFrom = it.first
                                                dateTo = it.second
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    if (showFromDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showFromDatePicker = false
                        // Здесь бы обработать выбранную дату
                        selectedQuickFilter = null
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFromDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            // DatePicker
        }
    }

    if (showToDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showToDatePicker = false
                        selectedQuickFilter = null
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showToDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            // DatePicker
        }
    }
}

@Composable
@Preview
private fun Preview() {
    DateFilterWithPickers()
}