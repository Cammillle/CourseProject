package com.alfabank.homework.courseproject.presentation.ui.feedScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen.firstUppercase

@Suppress("NonSkippableComposable")
@Composable
fun EventCard(
    event: Item,
    onClick: (Long) -> Unit,
    onBookmarkClick: (Item) -> Unit,
) {
    val isFavorite = event.isFavourite
   // val isFavorite by rememberSaveable { mutableStateOf(event.isFavourite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(event.id) })
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Фотография с кнопкой цены
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val images = event.images
                if (!images.isNullOrEmpty()) {
                    // Фоновое изображение
                    AsyncImage(
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop,
                        model = images[0]
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Кнопка цены на фото (внизу слева)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    val price = event.price
                    Text(
                        text = if (price.isNullOrEmpty()) "Бесплатно" else formatPrice(price),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Иконка сердечка в правом верхнем углу
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .clickable {
                            onBookmarkClick(event)
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Добавить в избранное",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Бейдж 6+ на фото (в левом верхнем углу)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    val age = event.ageRestriction
                    Text(
                        text = if (age.isNullOrEmpty()) "0+" else if (age == "0") "0+" else age,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Текстовая информация под фото
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val title = event.title
                val description = event.description
                val date = event.startDate
                val time = event.startTime

                // Заголовок
                Text(
                    text = "${title?.firstUppercase()}. ${description?.firstUppercase()}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Информация о дате и месте
                Column {
                    if (date != null) {
                        // Дата и время
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (time == null) date else "$date $time",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Место
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val placeTitle = event.placeTitle
                        if (placeTitle != null) {
                            Text(
                                text = placeTitle.firstUppercase(),
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Город
                    Text(
                        text = event.city,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}


fun formatPrice(original: String): String {
    val numbers = Regex("\\d+")
        .findAll(original)
        .map { it.value.toIntOrNull() }
        .filterNotNull()
        .toList()
    val max = numbers.lastOrNull()
    return when {
        max == null -> original
        max == 0 -> "Бесплатно"
        else -> "до $max ₽"
    }
}

