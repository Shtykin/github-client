package ru.shtykin.githubclient.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.shtykin.githubclient.domain.Repository
import ru.shtykin.githubclient.presentation.state.ScreenState

class MainViewModel(
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = mutableStateOf<ScreenState>(ScreenState.SplashScreenLoading)

    val uiState: State<ScreenState>
        get() = _uiState

    init {
        initLoading()
    }

    private fun initLoading() {
        Log.e("DEBUG1", "initLoading")
        viewModelScope.launch(Dispatchers.IO) {
        loadInitData()
            withContext(Dispatchers.Main){
                _uiState.value = ScreenState.SplashScreenLoaded
            }
        }
    }

    fun onMainScreenOpened() {
        Log.e("DEBUG1", "onMainScreenOpened")
        _uiState.value = ScreenState.MainScreen(
            repositories = emptyList()
        )
    }

    fun onDownloadScreenOpened() {
        Log.e("DEBUG1", "onDownloadScreenOpened")
        _uiState.value = ScreenState.DownloadsScreen("")
    }

    fun getUserRepositories(
        user: String,
        onFailed: ((String) -> Unit)?
        ) {
        _uiState.value = ScreenState.MainScreenLoading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val repositories = repository.getUserRepositories(user)
                Log.e("DEBUG1", "repo -> $repositories")
                withContext(Dispatchers.Main) {
                    _uiState.value = ScreenState.MainScreen(
                        repositories = repositories
                    )
                    if (repositories.isEmpty()) onFailed?.invoke("Not found")
                }
            } catch (e: Exception) {
                Log.e("DEBUG1", "e -> ${e.message}")
                withContext(Dispatchers.Main) {
                    _uiState.value = ScreenState.MainScreen(
                        repositories = emptyList()
                    )
                    onFailed?.invoke("ヽ(°° )ノ")
                }
            }
        }
    }

    private suspend fun loadInitData() {
        delay(5000)
    }

}