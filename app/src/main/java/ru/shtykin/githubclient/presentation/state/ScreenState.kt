package ru.shtykin.githubclient.presentation.state

import ru.shtykin.githubclient.domain.entity.UserRepositoryModel

sealed class ScreenState {
    sealed class MainScreenState() : ScreenState()
    object MainScreenLoading : MainScreenState()
    data class MainScreen(
        val repositories: List<UserRepositoryModel>
    ): MainScreenState()

    data class DownloadsScreen(
        val temp: String
    ) : ScreenState()

    sealed class SplashScreenState() : ScreenState()

    object SplashScreenLoading: SplashScreenState()
    object SplashScreenLoaded: SplashScreenState()

}
