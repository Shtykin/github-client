package ru.shtykin.githubclient.navigation

sealed class Screen(
    val route: String
) {
    data object Splash: Screen(ROUTE_SPLASH)
    data object Main: Screen(ROUTE_MAIN)
    data object Downloads: Screen(ROUTE_DOWNLOADS)

    private companion object {
        const val ROUTE_SPLASH = "route_splash"
        const val ROUTE_MAIN = "route_main"
        const val ROUTE_DOWNLOADS = "route_downloads"
    }
}
