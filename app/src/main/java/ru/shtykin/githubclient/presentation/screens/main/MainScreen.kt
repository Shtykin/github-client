package ru.shtykin.githubclient.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.shtykin.githubclient.R
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel
import ru.shtykin.githubclient.presentation.screens.common.HorizontalSpace
import ru.shtykin.githubclient.presentation.state.ScreenState
import ru.shtykin.githubclient.presentation.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: ScreenState,
    onDownloadClick: (() -> Unit)?,
    onSearchClick: ((String) -> Unit)?,
    onLinkClick: ((String) -> Unit)?,
    onDownloadRepositoryClick: ((String, String) -> Unit)?,
) {

    var user by remember { mutableStateOf(TextFieldValue(text = "")) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    TextField(
                        value = user,
                        onValueChange = { user = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Search user...",
                                color = Color.LightGray,
                            )
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        maxLines = 2,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (user.text.isNotEmpty()) onSearchClick?.invoke(user.text)
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            errorContainerColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onPrimary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                actions = {
                    if (user.text.isNotEmpty()) {
                        IconButton(onClick = {
                            user = TextFieldValue(text = "")
                        }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        IconButton(onClick = {
                            if (user.text.isNotEmpty()) onSearchClick?.invoke(
                                user.text
                            )
                        }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                    IconButton(onClick = { onDownloadClick?.invoke() }) {
                        Icon(
                            Icons.Outlined.Download,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            when (uiState) {
                is ScreenState.MainScreen -> {
                    val repositories = uiState.repositories
                    if (repositories.isNotEmpty()) {
                        LazyColumn(
                            contentPadding = paddingValues,
                            content = {
                                item {
                                    UserCard(
                                        name = repositories.first().user,
                                        avatarUrl = repositories.first().userAvatarUrl
                                    )
                                }
                                items(repositories) {
                                    RepositoryCard(
                                        repository = it,
                                        onLinkClick = onLinkClick,
                                        onDownloadRepositoryClick = onDownloadRepositoryClick
                                    )
                                }
                            }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.gh_logo_rw),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize(0.67f)
                                    .aspectRatio(1f),
                                colorFilter = ColorFilter.tint(color = LightGray)
                            )
                        }
                    }
                }

                is ScreenState.MainScreenLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                else -> {}
            }
        }
    )

}

@Composable
fun RepositoryCard(
    repository: UserRepositoryModel,
    onLinkClick: ((String) -> Unit)?,
    onDownloadRepositoryClick: ((String, String) -> Unit)?,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(0.1f),
            ) {
                Text(
                    text = repository.name,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    maxLines = 1,
                )
                Text(
                    text = "Last update: ${repository.lastUpdate}",
                    fontSize = 14.sp
                )
            }
            IconButton(
                onClick = { onLinkClick?.invoke(repository.htmlUrl) }
            ) {
                Icon(imageVector = Icons.Default.Link, contentDescription = null)
            }
            HorizontalSpace(width = 8.dp)
            IconButton(onClick = {
                onDownloadRepositoryClick?.invoke(repository.user, repository.name)
            }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.FileDownload,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun UserCard(
    name: String,
    avatarUrl: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = avatarUrl,
                placeholder = painterResource(R.drawable.baseline_person_64),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                fontSize = 22.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        HorizontalDivider()
    }
}