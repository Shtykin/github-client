package ru.shtykin.githubclient.presentation.state

import ru.shtykin.githubclient.domain.entity.ArchiveInfo
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel

sealed class ScreenState {
    sealed class MainScreenState() : ScreenState()
    data object MainScreenLoading : MainScreenState()
    data class MainScreen(
        val repositories: List<UserRepositoryModel>
    ): MainScreenState()

    data class DownloadsScreen(
        val archivesInfo: List<ArchiveInfo>
    ) : ScreenState()

    sealed class SplashScreenState() : ScreenState()

    data object SplashScreenLoading: SplashScreenState()
    data object SplashScreenLoaded: SplashScreenState()

}
