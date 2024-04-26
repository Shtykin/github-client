package ru.shtykin.githubclient.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.shtykin.githubclient.presentation.state.ScreenState

class MainViewModel(

) : ViewModel() {

    private val _uiState = mutableStateOf<ScreenState>(ScreenState.SplashScreen(""))

    val uiState: State<ScreenState>
        get() = _uiState

}