package com.alfabank.homework.courseproject.navigation

import com.alfabank.homework.courseproject.R

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: Int
) {
    object Home : NavigationItem(
        screen = Screen.Home,
        titleResId = R.string.navigation_item_main,
        icon = R.drawable.outline_feed_24
    )

    object Map : NavigationItem(
        screen = Screen.YandexMap,
        titleResId = R.string.navigation_item_map,
        icon = R.drawable.outline_map_24
    )

    object Favourite : NavigationItem(
        screen = Screen.Favourite,
        titleResId = R.string.navigation_item_favourite,
        icon = R.drawable.outline_favorite_24
    )

    object Profile : NavigationItem(
        screen = Screen.Profile,
        titleResId = R.string.navigation_item_profile,
        icon = R.drawable.outline_person_24
    )
}