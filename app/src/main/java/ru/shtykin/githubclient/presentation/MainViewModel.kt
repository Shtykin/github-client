package ru.shtykin.githubclient.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.shtykin.githubclient.presentation.state.ScreenState

class MainViewModel(

) : ViewModel() {

    private val _uiState = mutableStateOf<ScreenState>(ScreenState.SplashScreenLoading)

    val uiState: State<ScreenState>
        get() = _uiState

    fun initLoading() {
        viewModelScope.launch(Dispatchers.IO) {
        loadInitData()
            withContext(Dispatchers.Main){
                _uiState.value = ScreenState.SplashScreenLoaded
            }
        }
    }

    fun onMainScreenOpened() {
        _uiState.value = ScreenState.MainScreen("")
    }

    fun onDownloadScreenOpened() {
        _uiState.value = ScreenState.DownloadsScreen("")
    }

    private suspend fun loadInitData() {
        delay(4000)
    }

}