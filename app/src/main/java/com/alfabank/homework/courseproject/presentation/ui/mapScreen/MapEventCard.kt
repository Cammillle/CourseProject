package com.alfabank.homework.courseproject.presentation.ui.mapScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alfabank.homework.courseproject.domain.Item

@Suppress("NonSkippableComposable")
@Composable
fun MapEventCard(
    event: Item,
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onBuyTickets: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Заголовок и кнопка закрытия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val title = event.placeTitle ?: event.title ?: ""
                Text(
                    text = title.replace("+"," "),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Расписание и билеты
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Расписание и билеты",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Адрес
            Column {
                Text(
                    text = "Адрес",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                event.address?.let { address ->
                    Text(
                        text = address.replace("+"," "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

//                // Метро
//                event.place?.subway?.let { subway ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Subway,
//                            contentDescription = null,
//                            modifier = Modifier.size(14.dp),
//                            tint = Color(0xFF2196F3)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = subway,
//                            style = MaterialTheme.typography.bodySmall,
//                            color = Color(0xFF2196F3)
//                        )
//                    }
//                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Жанры и возраст
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = "Жанры",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                    Text(
//                        text = event.categories.joinToString(", "),
//                        style = MaterialTheme.typography.bodyMedium,
//                        maxLines = 2
//                    )
//                }
//
//                Column(horizontalAlignment = Alignment.End) {
//                    Text(
//                        text = "Возраст",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                    Text(
//                        text = event.ageRestriction,
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка покупки билетов
            Button(
                onClick = onBuyTickets,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Купить билеты")
            }
        }
    }
}