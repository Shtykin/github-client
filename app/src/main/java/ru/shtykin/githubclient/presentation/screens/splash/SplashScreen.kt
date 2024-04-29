package ru.shtykin.githubclient.presentation.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import ru.shtykin.githubclient.R
import ru.shtykin.githubclient.presentation.state.ScreenState

@Composable
fun SplashScreen(
    uiState: ScreenState,
    onInitLoading: (() -> Unit)?,
    onFinishLoading: (() -> Unit)?,
) {

    if ((uiState as? ScreenState.SplashScreenState) is ScreenState.SplashScreenLoading) {
        onInitLoading?.invoke()
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = null) {
            visible = true
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(4000)),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gh_logo_rw),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize(0.67f)
                            .aspectRatio(1f),
                    )
                }
            }
        }
    } else {
        onFinishLoading?.invoke()
    }
}