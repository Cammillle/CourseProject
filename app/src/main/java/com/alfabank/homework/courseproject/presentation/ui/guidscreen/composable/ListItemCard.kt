package com.alfabank.homework.courseproject.presentation.ui.guidscreen.composable

import androidx.compose.foundation.Image
import com.alfabank.homework.courseproject.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alfabank.homework.courseproject.presentation.ui.theme.*

@Composable
fun ListItemCard(
    title: String,
    description: String,
    imageUrls: List<String> = emptyList(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Grey3),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            /**Заголовок**/
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Grey1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            /***Описание*/
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Grey2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 12.dp)
            )


            if (imageUrls.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null
                )
            }

            when (imageUrls.size) {
                0 -> {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null
                    )
                }

                1 -> {
                    AsyncImage(
                        model = imageUrls[0],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_launcher_background)
                    )
                }

                else -> {
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        initialPageOffsetFraction = 0f
                    ) {
                        minOf(imageUrls.size, 3)
                    }

                    Box {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) { page ->
                            AsyncImage(
                                model = imageUrls[page],
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { index ->
                                val color =
                                    if (pagerState.currentPage == index) ProjectYellow else Grey4
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color)
                                        .padding(horizontal = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xE8121212)
@Composable
fun EventCollectionCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGrey)
                .padding(16.dp)
        ) {
            ListItemCard(
                title = "Летние фестивали 2025",
                description = "Самые яркие события этого лета: музыка, еда, искусство и не только. Успейте забронировать билеты!",
                imageUrls = listOf(
                    "https://media.kudago.com/images/event/82/bb/82bb410cca413007c5598f40c6ebd68c.jpg",
                    "https://media.kudago.com/images/event/82/bb/82bb410cca413007c5598f40c6ebd68c.jpg",
                    "https://example.com/image3.jpg",
                    "https://example.com/image4.jpg"
                ),
                onClick = { }
            )
        }
    }
}