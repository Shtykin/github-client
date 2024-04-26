package ru.shtykin.githubclient.presentation.state

sealed class ScreenState {
    data class MainScreen(
        val temp: String
    ) : ScreenState()

    data class DownloadsScreen(
        val temp: String
    ) : ScreenState()

    sealed class SplashScreenState() : ScreenState()

    object SplashScreenLoading: SplashScreenState()
    object SplashScreenLoaded: SplashScreenState()

}
