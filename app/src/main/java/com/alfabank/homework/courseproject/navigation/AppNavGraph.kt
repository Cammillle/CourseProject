package com.alfabank.homework.courseproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    eventsFeedScreenContent: @Composable () -> Unit,
    eventsFeedFiltersScreenContent: @Composable () -> Unit,
    feedScreenContent: @Composable (Long) -> Unit,
    favouriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    yandexMapScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        homeScreenNavGraph(
            eventsFeedScreenContent = eventsFeedScreenContent,
            eventsFeedFiltersScreenContent = eventsFeedFiltersScreenContent,
            feedScreenContent = feedScreenContent
        )
        composable(Screen.Favourite.route) {
            favouriteScreenContent()
        }
        composable(Screen.Profile.route) {
            profileScreenContent()
        }
        composable(Screen.YandexMap.route) {
            yandexMapScreenContent()
        }
    }

}