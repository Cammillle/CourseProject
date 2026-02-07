package com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.model.Event
import com.alfabank.homework.courseproject.presentation.ui.theme.BackgroundGrey
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey1
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey2
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey3
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey4
import com.alfabank.homework.courseproject.presentation.ui.theme.ProjectYellow

@Suppress("NonSkippableComposable")
@Composable
fun EventDetailScreen(
    paddingValues: PaddingValues,
    state: EventState
) {
    var isLiked by remember { mutableStateOf(false) }

    if (state.error == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGrey)
                .verticalScroll(rememberScrollState())
        ) {
            state.event?.let { event ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Event image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { /* Обработка навигации назад */ },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 52.dp)
                            .background(BackgroundGrey.copy(alpha = 0.8f), CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = { isLiked = !isLiked },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 52.dp)
                            .align(Alignment.TopEnd)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = ProjectYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Grey3,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "От 1500 ₽",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Grey1
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ProjectYellow
                            )
                        ) {
                            Text(
                                text = "Перейти на сайт",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Grey4),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "Date",
                                tint = ProjectYellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Пятница, март 13, 2026 19:00",
                            fontSize = 14.sp,
                            color = Grey1,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.5f))

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Grey4),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Location",
                                tint = ProjectYellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Дом Шрёдера",
                                fontSize = 14.sp,
                                color = Grey1,
                                fontWeight = FontWeight.Normal,
                            )
                            Text(
                                text = "Петроградская наб, дом 32.",
                                fontSize = 12.sp,
                                color = Grey2,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowRight,
                            contentDescription = "Navigate",
                            tint = Grey1,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.5f))
                }
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Описание мероприятия",
                        fontSize = 12.sp,
                        color = Grey2,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        fontSize = 14.sp,
                        color = Grey1,
                        fontWeight = FontWeight.Normal,
                        text =
                            "djskfjskfjdkfjkdsjfdsjf jkfjskfjdskfjskdlf " +
                                    "jsfsjfksldfj  ksjfklsjfdksljf fklsjfks jkfsjsk " +
                                    "jfksfjksdjf ksjfksjfksjkfsdjkfdjskfdsjkfdsjfksdjkfsjkfjsdkfjsdlkfjsf"
                    )
                }
            }
        }
    }
}

