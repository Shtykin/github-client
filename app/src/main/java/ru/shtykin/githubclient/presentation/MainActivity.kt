package ru.shtykin.githubclient.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                            onDownloadRepositoryClick = {user, repositoryName ->
                                if (checkStoragePermission()) downloadRepo(user, repositoryName)
                            }
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

    @SuppressLint("Range")
    private fun downloadRepo(user: String, repositoryName: String) {
        val url = "https://api.github.com/repos/${user}/${repositoryName}/zipball/"
        val request = DownloadManager
            .Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${user}_${repositoryName}.zip")

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)
            var progress = -1L

        lifecycleScope.launch(Dispatchers.IO) {
                try {
                    var downloading = true
                    while (downloading) {
                        val q = DownloadManager.Query()
                        q.setFilterById(downloadId)
                        val cursor: Cursor = downloadManager.query(q)
                        cursor.moveToFirst()
                        val bytesDownloaded =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val bytesTotal =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false
                        }
                        progress = 100L * bytesDownloaded / bytesTotal
                    }
                } catch (e: Exception) {

                }

            }

    }

    private fun checkStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) return true
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, permission
            ) -> {
                Toast.makeText(
                    this,
                    "To download a repository you should provide permission to the storage",
                    Toast.LENGTH_LONG
                ).show()
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
                ActivityCompat.requestPermissions(this, arrayOf(permission),RC_REQUEST_PERMISSION)
//                requestPermissions(
//                    arrayOf(permission),
//                    RC_REQUEST_PERMISSION
//                )
            }
        }
        return false
    }

    companion object {
        private const val RC_REQUEST_PERMISSION = 111
    }
}