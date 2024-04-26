package ru.shtykin.githubclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.shtykin.githubclient.navigation.AppNavGraph
import ru.shtykin.githubclient.navigation.Screen
import ru.shtykin.githubclient.presentation.screens.downloads.DownloadsScreen
import ru.shtykin.githubclient.presentation.screens.main.MainScreen
import ru.shtykin.githubclient.presentation.screens.splash.SplashScreen
import ru.shtykin.githubclient.presentation.theme.GitHubClientTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            val uiState by viewModel.uiState
            val startScreenRoute = Screen.Splash.route
            GitHubClientTheme {
                AppNavGraph(
                    startScreenRoute = startScreenRoute,
                    navHostController = navHostController,
                    splashScreenContent = { SplashScreen(
                        uiState = uiState,
                    ) },
                    mainScreenContent = { MainScreen(
                        uiState = uiState,
                    ) },
                    downloadsScreenContent = { DownloadsScreen(
                        uiState = uiState,
                    ) },
                )
            }
        }
    }
}