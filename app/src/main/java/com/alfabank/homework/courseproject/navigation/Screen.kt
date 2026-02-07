package com.alfabank.homework.courseproject.navigation

sealed class Screen(
    val route: String
) {
    object Home : Screen(ROUTE_HOME) //вложенный граф навигации

    object EventsFeedFilters : Screen(ROUTE_EVENT_FILTERS)
    object EventsListFeed : Screen(ROUTE_EVENTS_FEED)
    object Event : Screen("$ROUTE_EVENT/{event_id}") {
        fun getRouteWithArgs(id: Long): String {
            return "$ROUTE_EVENT/$id"
        }
    }

    object Favourite : Screen(ROUTE_FAVOURITE)
    object YandexMap : Screen(ROUTE_YANDEX_MAP)
    object Profile : Screen(ROUTE_PROFILE)


    companion object {
        const val KEY_EVENT_ID= "event_id"

        const val ROUTE_HOME = "home"
        const val ROUTE_EVENT_FILTERS = "home_filters"

        const val ROUTE_EVENTS_FEED = "events_feed"
        const val ROUTE_EVENT = "event"

        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_YANDEX_MAP = "yandex_map"
        const val ROUTE_PROFILE = "profile"
    }

}