package com.alfabank.homework.courseproject.presentation.ui.homescreen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.alfabank.homework.courseproject.navigation.AppNavGraph
import com.alfabank.homework.courseproject.navigation.NavigationItem
import com.alfabank.homework.courseproject.navigation.Screen
import com.alfabank.homework.courseproject.navigation.rememberNavigationState
import com.alfabank.homework.courseproject.presentation.ui.EventViewModel
import com.alfabank.homework.courseproject.presentation.ui.favouritescreen.FavouriteScreen
import com.alfabank.homework.courseproject.presentation.ui.favouritescreen.FavouriteViewModel
import com.alfabank.homework.courseproject.presentation.ui.filterScreen.EventsFilterScreen
import com.alfabank.homework.courseproject.presentation.ui.feedScreen.FeedScreen
import com.alfabank.homework.courseproject.presentation.ui.homescreen.components.FeedTopBar
import com.alfabank.homework.courseproject.presentation.ui.homescreen.eventScreen.EventDetailScreen
import com.alfabank.homework.courseproject.presentation.ui.guidscreen.GuidScreen
import com.alfabank.homework.courseproject.presentation.ui.mapScreen.MapScreen
import com.alfabank.homework.courseproject.presentation.ui.profilescreen.ProfileScreen

@Composable
fun MainScreen() {
    val viewModel: EventViewModel = viewModel()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val favouriteViewModel: FavouriteViewModel = viewModel()
    val favouriteState = favouriteViewModel.state.collectAsStateWithLifecycle()

    val currentPagingFlow = viewModel.currentPagingFlow.collectAsLazyPagingItems()
    val isRefreshing = currentPagingFlow.loadState.refresh is LoadState.Loading
    val searchList by viewModel.searchResults.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    Log.d("MainScreen", "$navBackStackEntry")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (
                currentDestination == Screen.EventsListFeed.route
            ) {
                FeedTopBar(
                    navigateOnFilterScreen = {
                        navigationState.navigateTo(Screen.EventsFeedFilters.route)
                    },
                    onSearchQueryChanged = { query ->
                        viewModel.searchEvent(query)
                    },
                    onRefreshSearch = { viewModel.refreshSearchList() },
                )
            }
        },
        bottomBar = {
            if (navBackStackEntry != null) {
                if (currentDestination != Screen.EventsFeedFilters.route
                    && (currentDestination != Screen.Event.route) && (currentDestination != Screen.YandexMap.route)
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        val items = listOf(
                            NavigationItem.Home,
                            NavigationItem.Map,
                            NavigationItem.Favourite,
                            NavigationItem.Profile
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
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                    indicatorColor = Color.Transparent
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
                    paddingValues = paddingValues,
                    onEventClick = { id ->
                        navigationState.navigateToEventDetails(id = id)
                    },
                    selectedCategory = selectedCategory,
                    onSelectCategory = { category ->
                        viewModel.selectCategory(category)
                    },
                    isRefreshing = isRefreshing,
                    currentPagingFlow = currentPagingFlow,
                    searchList = searchList,
                    onBookmarkClick = { event ->
                        val dd = favouriteViewModel.isBookmarked(event.id)
                        if (dd) {
                            favouriteViewModel.addBookmark(event)
                        } else {
                            favouriteViewModel.removeBookmark(event.id)
                        }
                    },
                    isBookmark = {
                        val fav = favouriteState.value.items.orEmpty().any { it.id == it.id }
                        fav
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
            favouriteScreenContent = {

                FavouriteScreen(
                    items = favouriteState.value.items ?: emptyList()
                )
            },
            profileScreenContent = { ProfileScreen() },
            guidsScreenContent = {
                GuidScreen(
                    onMapNavigate = { mapArgs ->
                        navigationState.navigateToYandexMap(mapArgs)
                    }
                )
            },
            feedScreenContent = { id ->
                EventDetailScreen(
                    onBackClick = { navigationState.navHostController.popBackStack() },
                    onAddFavourite = {},
                    onMapNavigate = { mapArgs ->
                        navigationState.navigateToYandexMap(mapArgs)
                    }
                )
            },
            yandexMapScreenContent = { mapArgs ->
                MapScreen(
                    mapArgs,
                    onBack = { navigationState.navHostController.popBackStack() }
                )
            }
        )
        Log.d("MainScreen", "${navigationState.navHostController}")

    }
}