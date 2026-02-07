package com.alfabank.homework.courseproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument

fun NavGraphBuilder.homeScreenNavGraph(
    eventsFeedScreenContent: @Composable () -> Unit,
    feedScreenContent: @Composable (Long) -> Unit,
    eventsFeedFiltersScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.EventsListFeed.route,
        route = Screen.Home.route
    ) {
        composable(
            route = Screen.Event.route,
            arguments = listOf(navArgument(Screen.KEY_EVENT_ID) {
                type = NavType.LongType
            })
        ) {
            val id =
                it.arguments?.getLong(Screen.KEY_EVENT_ID) ?: throw RuntimeException("Args is null")
            feedScreenContent(id)
        }
        composable(Screen.EventsListFeed.route) {
            eventsFeedScreenContent()
        }
        composable(Screen.EventsFeedFilters.route) {
            eventsFeedFiltersScreenContent()
        }
    }
}