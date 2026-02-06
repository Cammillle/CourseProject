package com.alfabank.homework.courseproject.presentation.ui.filterScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesFilter() {
    var expanded by remember { mutableStateOf(true) }
    val categories = listOf(
        "Балет", "Бизнес", "Вечеринки", "Выставки", "Детям", "Здоровье",
        "Кино", "Концерты", "Спорт", "Фестивали", "Театры", "Шоу", "Экскурсии"
    )
    val categories1 = listOf(
        "Балет", "Бизнес", "Вечеринки", "Выставки", "Детям", "Здоровье",
        "Кино"
    )
    val categories2 = listOf(
        "Концерты", "Спорт", "Фестивали", "Театры", "Шоу", "Экскурсии"
    )
    var selectedCategories by remember {
        mutableStateOf(
            mutableSetOf(
                "Все", "Балет", "Бизнес", "Вечеринки", "Выставки", "Детям", "Здоровье",
                "Кино", "Концерты", "Спорт", "Фестивали", "Театры", "Шоу", "Экскурсии"
            )
        )

    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    text = "Категории",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Свернуть" else "Развернуть",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Раскрывающийся список
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // Чекбокс "Все"
                    CategoryCheckbox(
                        text = "Все",
                        selected = selectedCategories.contains("Все") &&
                                categories.all { it in selectedCategories },
                        onClick = {
                            val newSelected = mutableSetOf<String>()

                            val isAllSelected = selectedCategories.contains("Все") &&
                                    categories.all { it in selectedCategories }

                            if (isAllSelected) {
                                selectedCategories = mutableSetOf()
                            } else {
                                newSelected.add("Все")
                                newSelected.addAll(categories)
                                selectedCategories = newSelected
                            }
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(270.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            categories1.forEach { category ->
                                CategoryCheckbox(
                                    text = category,
                                    selected = selectedCategories.contains(category),
                                    onClick = {
                                        val newSelected = selectedCategories.toMutableSet()
                                        if (newSelected.contains(category)) {
                                            newSelected.remove(category)
                                            if (newSelected.contains("Все")) newSelected.remove("Все")
                                        } else {
                                            newSelected.add(category)
                                            if (newSelected.containsAll(categories)) {
                                                newSelected.add("Все")
                                            }
                                        }
                                        selectedCategories = newSelected
                                    }
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            categories2.forEach { category ->
                                CategoryCheckbox(
                                    text = category,
                                    selected = selectedCategories.contains(category),
                                    onClick = {
                                        val newSelected = selectedCategories.toMutableSet()
                                        if (newSelected.contains(category)) {
                                            newSelected.remove(category)
                                            if (newSelected.contains("Все")) newSelected.remove("Все")
                                        } else {
                                            newSelected.add(category)
                                            if (newSelected.containsAll(categories)) {
                                                newSelected.add("Все")
                                            }
                                        }
                                        selectedCategories = newSelected
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    CategoriesFilter()
}