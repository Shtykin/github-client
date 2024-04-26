package ru.shtykin.githubclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    startScreenRoute: String,
    navHostController: NavHostController,
    splashScreenContent: @Composable () -> Unit,
    mainScreenContent: @Composable () -> Unit,
    downloadsScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startScreenRoute
    ) {
        composable(Screen.Splash.route) {
            splashScreenContent()
        }
        composable(Screen.Main.route) {
            mainScreenContent()
        }
        composable(Screen.Downloads.route) {
            downloadsScreenContent()
        }
    }

}