package ru.shtykin.githubclient.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                darkTheme = false, dynamicColor = false
            ) {
                AppNavGraph(
                    startScreenRoute = startScreenRoute,
                    navHostController = navHostController,
                    splashScreenContent = {
                        SplashScreen(uiState = uiState, onInitLoading = { },
                            onFinishLoading = {
                                viewModel.onMainScreenOpened()
                                navHostController.navigate(Screen.Main.route) {
                                    popUpTo(0)
                                }
                            })
                    },
                    mainScreenContent = {
                        MainScreen(uiState = uiState, onDownloadClick = {
                            viewModel.onDownloadScreenOpened()
                            navHostController.navigate(Screen.Downloads.route) {
                                popUpTo(Screen.Main.route)
                            }
                        }, onSearchClick = { user ->
                            viewModel.getUserRepositories(user = user, onFailed = { msg ->
                                Toast.makeText(
                                    this@MainActivity, msg, Toast.LENGTH_LONG
                                ).show()
                            })
                        }, onLinkClick = { url ->
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(browserIntent)
                        }, onDownloadRepositoryClick = { user, repositoryName ->
                            if (ContextCompat.checkSelfPermission(
                                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                downloadRepo(user, repositoryName)
                            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                                downloadRepo(user, repositoryName)
                            } else {
                                requestStoragePermissions()
                            }
                        })
                    },
                    downloadsScreenContent = {
                        DownloadsScreen(
                            uiState = uiState,
                            onDeleteClick = viewModel::clearAllData,
                            onBackClick = {
                                viewModel.onMainScreenOpened()
                                navHostController.popBackStack()
                            })
                    },
                )
            }
        }
    }

    @SuppressLint("Range")
    private fun downloadRepo(user: String, repositoryName: String) {
        val stringUrl = "https://api.github.com/repos/${user}/${repositoryName}/zipball/"

        val request = DownloadManager.Request(Uri.parse(stringUrl))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "${user}_${repositoryName}.zip"
            )

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        viewModel.insertArchiveInfoToDb(
            user = user,
            name = repositoryName,
        )
    }

    private fun requestStoragePermissions() {
        val builder = AlertDialog.Builder(this).apply {
            setCancelable(true)
            setTitle("Доступ к хранилищу")
            setMessage("Для скачивания предоставьте GitHub client доступ к хранилищу и повторите загрузку")
            setPositiveButton(
                "Предоставить доступ"
            ) { _, _ ->
                when {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) -> {
                        val settingsIntent = Intent().also {
                            it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            it.addCategory(Intent.CATEGORY_DEFAULT)
                            it.data = Uri.parse("package:$packageName")
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            it.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            it.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        }
                        startActivity(settingsIntent)
                    }

                    else -> {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            RC_REQUEST_PERMISSION
                        )
                    }
                }
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    companion object {
        private const val RC_REQUEST_PERMISSION = 111
    }
}