package com.alfabank.homework.courseproject.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.alfabank.homework.courseproject.presentation.ui.homescreen.MainScreen
import com.alfabank.homework.courseproject.presentation.ui.theme.CourseProjectTheme
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapKitFactory.setApiKey("bc0b2b9d-9fdf-46cd-828f-95a834344db5")
        MapKitFactory.initialize(this)
        setContent {
            CourseProjectTheme {
                Surface {
                    MainScreen()
                }
            }
        }
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}


