package ru.shtykin.githubclient.presentation

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
import java.util.Date

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
        viewModelScope.launch(Dispatchers.IO) {
            loadInitData()
            withContext(Dispatchers.Main) {
                _uiState.value = ScreenState.SplashScreenLoaded
            }
        }
    }

    fun onMainScreenOpened() {
        _uiState.value = ScreenState.MainScreen(
            repositories = emptyList()
        )
    }

    fun onDownloadScreenOpened() {
        _uiState.value = ScreenState.DownloadsScreen(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFlowInfoAllArchive().collect {
                if (_uiState.value is ScreenState.DownloadsScreen) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = ScreenState.DownloadsScreen(it.reversed())
                    }
                }
            }
        }
    }

    fun insertArchiveInfoToDb(
        user: String,
        name: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertInfoData(
                user = user,
                name = name,
                date = Date()
            )
        }
    }

    fun getUserRepositories(
        user: String,
        onFailed: ((String) -> Unit)?
    ) {
        _uiState.value = ScreenState.MainScreenLoading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val repositories = repository.getUserRepositories(user)
                withContext(Dispatchers.Main) {
                    _uiState.value = ScreenState.MainScreen(
                        repositories = repositories
                    )
                    if (repositories.isEmpty()) onFailed?.invoke("Not found")
                }
            } catch (e: Exception) {
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
        val loadJob = viewModelScope.launch(Dispatchers.IO) {
            //load data
            delay(8000)
        }
        val splashJob = viewModelScope.launch(Dispatchers.IO) {
            //for splash screen
            delay(4000)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadJob.join()
            splashJob.join()
        }.join()
    }

}