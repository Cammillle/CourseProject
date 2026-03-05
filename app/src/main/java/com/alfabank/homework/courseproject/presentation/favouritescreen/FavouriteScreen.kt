package com.alfabank.homework.courseproject.presentation.favouritescreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.model.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    items: List<Item> = emptyList(),
    onItemClick: (Long) -> Unit
) {
    Log.d("Favoruite screen", "Items $items")
    val itemsByCategory = items.groupBy { item ->
        item.categories?.getOrNull(0)?.convertCategory() ?: "Другое"
    }
    Log.d("Favoruite screen", "Map $itemsByCategory")


    val mapImages = mapOf(
        "Концерты" to R.drawable.concert,
        "Спектакли" to R.drawable.theater,
        "Экскурсии" to R.drawable.tour,
        "Ярмарки" to R.drawable.yarmarki,
        "Активный отдых" to R.drawable.recreation,
        "Выставки" to R.drawable.exhibition,
        "Фестивали" to R.drawable.festival
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Избранное",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(itemsByCategory.entries.toList()) { (category, categoryItem) ->
                CategoryRow(
                    categoryName = category,
                    imageId = mapImages[category] ?: R.drawable.festival,
                    items = categoryItem,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
fun CategoryRow(
    onItemClick: (Long) -> Unit,
    categoryName: String,
    imageId: Int,
    items: List<Item>
) {
    Log.d("Favoruite screen", "Items $items")

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.Absolute.spacedBy(4.dp)
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(imageId),
                contentDescription = null
            )
            //Заголовок
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                Log.d("Favoruite screen", "Item $item")
                CategoryItemCard(
                    item,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
fun CategoryItemCard(
    item: Item,
    onItemClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = { onItemClick(item.id) })
            .width(160.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(item.images?.getOrNull(0))
                .memoryCacheKey("list-image-${item.id}")
                .placeholderMemoryCacheKey("list-image-${item.id}")
                .build()

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                model = imageRequest,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Описание
            Text(
                text = item.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun String.convertCategory(): String {
    val categoryMap = mapOf(
        "concert" to "Концерты",
        "theater" to "Спектакли",
        "tour" to "Экскурсии",
        "yarmarki-razvlecheniya-yarmarki" to "Ярмарки",
        "recreation" to "Активный отдых",
        "exhibition" to "Выставки",
        "festival" to "Фестивали",
        "kids" to "Для детей"
    )
    return categoryMap[this] ?: "Концерты"
}