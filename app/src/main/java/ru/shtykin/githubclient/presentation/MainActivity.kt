package ru.shtykin.githubclient.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
            GitHubClientTheme(
                darkTheme = false
            ) {
                AppNavGraph(
                    startScreenRoute = startScreenRoute,
                    navHostController = navHostController,
                    splashScreenContent = {
                        SplashScreen(
                            uiState = uiState,
                            onInitLoading = { },
//                            onInitLoading = { viewModel.initLoading() },
                            onFinishLoading = {
                                viewModel.onMainScreenOpened()
                                navHostController.navigate(Screen.Main.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    },
                    mainScreenContent = {
                        MainScreen(
                            uiState = uiState,
                            onDownloadClick = {
                                viewModel.onDownloadScreenOpened()
                                navHostController.navigate(Screen.Downloads.route) {
                                    popUpTo(Screen.Main.route)
                                }
                            },
                            onSearchClick = { user ->
                                viewModel.getUserRepositories(
                                    user = user,
                                    onFailed = { msg ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            msg,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            },
                            onLinkClick = {url ->
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(browserIntent)
                            },
                        )
                    },
                    downloadsScreenContent = {
                        DownloadsScreen(
                            uiState = uiState,
                            onBackClick = {
                                viewModel.onMainScreenOpened()
                                navHostController.popBackStack()
                            }
                        )
                    },
                )
            }
        }
    }
}