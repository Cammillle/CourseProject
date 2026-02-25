package com.alfabank.homework.courseproject.navigation

sealed class Screen(
    val route: String
) {
    object Home : Screen(ROUTE_HOME) //вложенный граф навигации
    object Guids : Screen(ROUTE_GUIDS)

    object YandexMap : Screen("$ROUTE_YANDEX_MAP/{$KEY_MAP_ARGS}") {
        fun getRouteWithMapArgs(mapArgs: String): String {
            return "$ROUTE_YANDEX_MAP/$mapArgs"
        }
    }

    object EventsFeedFilters : Screen(ROUTE_EVENT_FILTERS)
    object EventsListFeed : Screen(ROUTE_EVENTS_FEED)
    object Event : Screen("$ROUTE_EVENT/{$KEY_EVENT_ID}") {
        fun getRouteWithArgs(id: Long): String {
            return "$ROUTE_EVENT/$id"
        }
    }

    object Favourite : Screen(ROUTE_FAVOURITE)

    object Profile : Screen(ROUTE_PROFILE)


    companion object {
        const val KEY_EVENT_ID = "event_id"
        const val KEY_MAP_ARGS = "map_args"


        const val ROUTE_HOME = "home"
        const val ROUTE_EVENT_FILTERS = "home_filters"

        const val ROUTE_EVENTS_FEED = "events_feed"
        const val ROUTE_EVENT = "event"

        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_GUIDS = "guids"
        const val ROUTE_YANDEX_MAP = "yandex_map"

        const val ROUTE_PROFILE = "profile"
    }

}