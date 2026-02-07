package com.alfabank.homework.courseproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.homeScreenNavGraph(
    eventsFeedScreenContent: @Composable () -> Unit,
    feedScreenContent: @Composable () -> Unit,
    eventsFeedFiltersScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.EventsListFeed.route,
        route = Screen.Home.route
    ) {
        composable(
            Screen.Event.route
        ) {
            feedScreenContent()
        }
        composable(Screen.EventsListFeed.route) {
            eventsFeedScreenContent()
        }
        composable(Screen.EventsFeedFilters.route) {
            eventsFeedFiltersScreenContent()
        }
    }
}