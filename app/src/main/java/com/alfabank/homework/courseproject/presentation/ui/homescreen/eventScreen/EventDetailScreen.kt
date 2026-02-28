package com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.alfabank.homework.courseproject.R
import com.alfabank.homework.courseproject.domain.Item
import com.alfabank.homework.courseproject.navigation.MapScreenArgs
import com.alfabank.homework.courseproject.presentation.ui.theme.BackgroundGrey
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey1
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey2
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey3
import com.alfabank.homework.courseproject.presentation.ui.theme.Grey4
import com.alfabank.homework.courseproject.presentation.ui.theme.ProjectYellow

@Composable
fun EventDetailScreen(
    onBackClick: () -> Unit,
    onAddFavourite: (Item) -> Unit,
    onMapNavigate: (MapScreenArgs) -> Unit
) {
    //передача id ивента через SavedStateHandle, достаем из вьюмодели
    val viewModel: EventDetailsViewModel = viewModel()
    val eventState = viewModel.eventState.collectAsStateWithLifecycle()
    val state = eventState.value


    Log.d("TAGTAG", "State screen $state ")

    if (state.error == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGrey)
                .verticalScroll(rememberScrollState())
        ) {
            state.selectedEvent?.let { event ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val images = event.images
                    if (!images.isNullOrEmpty()) {
                        val image = images[0]
                        AsyncImage(
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                            contentScale = ContentScale.Crop,
                            model = image
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Event image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { onBackClick() },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 52.dp)
                            .background(BackgroundGrey, CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    var isFavourite by remember { mutableStateOf(event.isFavourite) }
                    IconButton(
                        onClick = {
                            onAddFavourite(event)
                            isFavourite = !isFavourite
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 52.dp)
                            .align(Alignment.TopEnd)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
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
                        val price = if (event.price.isNullOrEmpty()) "" else event.price.replace(
                            "рублей",
                            "₽"
                        )
                        Text(
                            text = if (price.isEmpty()) "Уточняйте цену на сайте"
                            else price.firstUppercase(),
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


                        val isEndless = event.isEndless
                        if (isEndless != null && isEndless) {
                            Text(
                                text = "С расписанием",
                                fontSize = 14.sp,
                                color = Grey1,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            val startDate = event.startDate
                            val startTime = event.startTime
                            Text(
                                text = if (startDate.isNullOrEmpty()) "Дату уточняйте на сайте"
                                else if (startTime.isNullOrEmpty()) startDate
                                else "$startDate $startTime",
                                fontSize = 14.sp,
                                color = Grey1,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.5f))

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onMapNavigate(MapScreenArgs.SingleItem(event))
                            }) {
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
                            val placeTitle = event.placeTitle
                            placeTitle?.let { title ->
                                Text(
                                    text = title.firstUppercase(),
                                    fontSize = 14.sp,
                                    color = Grey1,
                                    fontWeight = FontWeight.Normal,
                                )
                                Text(
                                    text = event.address?.firstUppercase() ?: "Адрес неизвестен",
                                    fontSize = 12.sp,
                                    color = Grey2,
                                    fontWeight = FontWeight.Normal,
                                )
                            } ?: Text(
                                text = "Нет адреса",
                                fontSize = 14.sp,
                                color = Grey1,
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
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Описание мероприятия",
                        fontSize = 12.sp,
                        color = Grey2,
                        fontWeight = FontWeight.Normal
                    )
                    val bodyText =
                        if (event.bodyText.isNullOrEmpty()) "" else event.bodyText.firstUppercase()
                    Text(
                        fontSize = 14.sp,
                        color = Grey1,
                        fontWeight = FontWeight.Normal,
                        text = "${event.title?.firstUppercase()} \n" + "${event.description?.firstUppercase()} \n " + bodyText
                    )
                }
            }

        }
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text(
                text = state.error.toString(), color = MaterialTheme.colorScheme.error
            )
        }
    }
}

fun String.firstUppercase(): String {
    return this.replaceFirstChar {
        it.uppercaseChar().toString()
    }
}