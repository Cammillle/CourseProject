package com.alfabank.homework.courseproject.presentation.ui.homescreen

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alfabank.homework.courseproject.navigation.AppNavGraph
import com.alfabank.homework.courseproject.navigation.NavigationItem
import com.alfabank.homework.courseproject.navigation.Screen
import com.alfabank.homework.courseproject.navigation.rememberNavigationState
import com.alfabank.homework.courseproject.presentation.ui.EventViewModel
import com.alfabank.homework.courseproject.presentation.ui.favouritescreen.FavouriteScreen
import com.alfabank.homework.courseproject.presentation.ui.filterScreen.EventsFilterScreen
import com.alfabank.homework.courseproject.presentation.ui.feedScreen.FeedScreen
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FeedTopBar
import com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen.EventDetailScreen
import com.alfabank.homework.courseproject.presentation.ui.mapscreen.MapScreen
import com.alfabank.homework.courseproject.presentation.ui.profilescreen.ProfileScreen

@Composable
fun MainScreen() {
    val viewModel: EventViewModel = viewModel()
    val homeState = viewModel.homeState.collectAsStateWithLifecycle()

    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    Log.d("MainScreen", "$navBackStackEntry")

    Scaffold(
        topBar = {
            if (
                currentDestination == Screen.EventsListFeed.route
            ) {
                FeedTopBar(
                    navigateOnFilterScreen = {
                        navigationState.navigateTo(Screen.EventsFeedFilters.route)
                    }
                )
            }
        },
        bottomBar = {
            if (navBackStackEntry != null) {
                if (currentDestination != Screen.EventsFeedFilters.route
                    && (currentDestination != Screen.Event.route)
                ) {
                    NavigationBar {
                        val items = listOf(
                            NavigationItem.Map,
                            NavigationItem.Home,
                            NavigationItem.Profile,
                            NavigationItem.Favourite
                        )
                        items.forEach { item ->
                            val selected = navBackStackEntry?.destination?.hierarchy?.any {
                                it.route == item.screen.route
                            } ?: false

                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        navigationState.navigateTo(item.screen.route)
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(item.icon),
                                        contentDescription = null
                                    )
                                },
                                label = { Text(text = stringResource(item.titleResId)) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                                    selectedTextColor = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                }

            }
        }) { paddingValues ->

        AppNavGraph(
            navHostController = navigationState.navHostController,
            eventsFeedScreenContent = {
                FeedScreen(
                    events = homeState.value.events,
                    nextDataIsLoading = homeState.value.nextDataIsLoading,
                    loadNextEvents = { viewModel.loadNextEvents() },
                    paddingValues = paddingValues,
                    onClick = { id ->
                        navigationState.navigateToEventDetails(id = id)
                    }
                )
            },
            eventsFeedFiltersScreenContent = {
                EventsFilterScreen(
                    onBackClick = { navigationState.navHostController.popBackStack() },
                    onClearAll = {},
                    onSaveFiltersClick = { navigationState.navHostController.popBackStack() },
                    onResetFilters = { navigationState.navHostController.popBackStack() }
                )
            },
            favouriteScreenContent = { FavouriteScreen() },
            profileScreenContent = { ProfileScreen() },
            yandexMapScreenContent = { MapScreen() },
            feedScreenContent = { id ->
                EventDetailScreen(
                    onBackClick = { navigationState.navHostController.popBackStack() },
                    eventId = id
                )
            }
        )
        Log.d("MainScreen", "${navigationState.navHostController}")

    }
}