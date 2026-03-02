package com.alfabank.homework.courseproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToEventDetails(id: Long) {
        navHostController.navigate(route = Screen.Event.getRouteWithArgs(id = id))
    }

    fun navigateToYandexMap(mapArgs: MapScreenArgs) {
        val json = Json.encodeToString(mapArgs)
        val encodedJson = java.net.URLEncoder.encode(json, "UTF-8")
        navHostController.navigate(route = Screen.YandexMap.getRouteWithMapArgs(encodedJson))
    }
}


@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}