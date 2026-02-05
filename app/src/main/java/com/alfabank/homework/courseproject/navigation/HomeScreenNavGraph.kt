package com.alfabank.homework.courseproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.homeScreenNavGraph(
    eventsFeedScreenContent: @Composable () -> Unit,
    eventsFeedFiltersScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.EventsFeed.route,
        route = Screen.Home.route
    ) {
        composable(Screen.EventsFeed.route) {
            eventsFeedScreenContent()
        }
        composable(Screen.EventsFeedFilters.route) {
            eventsFeedFiltersScreenContent()
        }
    }
}