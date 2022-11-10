package com.lalosapps.freetoplay.ui.screens.game_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import com.lalosapps.freetoplay.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.ui.components.*

@ExperimentalPagerApi
@ExperimentalLifecycleComposeApi
@Composable
fun GameDetailsScreen(
    onBackPress: () -> Unit,
    onGameUrlClick: (String) -> Unit,
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    when (val uiState = viewModel.game.collectAsStateWithLifecycle().value) {
        Resource.Loading -> LoadingScreen()
        is Resource.Error -> ErrorScreen(
            uiState.error.toString(),
            onBackPress = onBackPress
        )
        is Resource.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier,
                        backgroundColor = MaterialTheme.colors.background,
                        contentColor = MaterialTheme.colors.primary,
                        onClick = {
                            viewModel.toggleFavorite(
                                uiState.data.id,
                                uiState.data.isFavorite
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (uiState.data.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            ) {
                GameDetails(
                    modifier = Modifier.padding(it),
                    gameDetails = uiState.data,
                    onBackPress = onBackPress,
                    onGameUrlClick = onGameUrlClick
                )
            }
        }
    }
}


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onBackPress: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GameDetailsNavBar(
            title = "",
            onBackPress = onBackPress
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_connection_error),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, textAlign = TextAlign.Center)
        }
    }
}

@ExperimentalPagerApi
@Composable
fun GameDetails(
    gameDetails: GameDetails,
    onBackPress: () -> Unit,
    onGameUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        GameDetailsNavBar(
            title = gameDetails.title,
            onBackPress = onBackPress
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (gameDetails.screenshots.isEmpty()) {
                NetworkImage(
                    url = gameDetails.thumbnail,
                    crossFade = 1000,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(250.dp),
                    onLoading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    onError = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                )
            } else {
                val urls = remember { gameDetails.screenshots.map { it.image } }
                CarouselView(
                    urls = urls,
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(250.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = MaterialTheme.shapes.medium,
                    crossFade = 1000
                )
            }
            Text(
                text = "About ${gameDetails.title}",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(16.dp)
            )
            ExpandableText(
                text = gameDetails.description,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Extra",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(16.dp)
            )
            GameDetailsExtraInfo(
                firstTitle = "Title",
                secondTitle = "Developer",
                textColor = MaterialTheme.colors.onSurface,
            )
            GameDetailsExtraInfo(
                firstTitle = gameDetails.title,
                secondTitle = gameDetails.developer,
                textColor = MaterialTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            GameDetailsExtraInfo(
                firstTitle = "Publisher",
                secondTitle = "Release date",
                textColor = MaterialTheme.colors.onSurface,
            )
            GameDetailsExtraInfo(
                firstTitle = gameDetails.publisher,
                secondTitle = gameDetails.releaseDate,
                textColor = MaterialTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            GameDetailsExtraInfo(
                firstTitle = "Genre",
                secondTitle = "Platform",
                textColor = MaterialTheme.colors.onSurface,
            )
            GameDetailsExtraInfo(
                firstTitle = gameDetails.genre,
                secondTitle = gameDetails.platform,
                textColor = MaterialTheme.colors.onSurface,
                icon = { Platform(platform = gameDetails.platform) }
            )
            gameDetails.minSystemRequirements?.let {
                if (!it.isNull) {
                    Text(
                        text = "Minimum System Requirements",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Processor",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = gameDetails.minSystemRequirements.processor ?: "?",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Memory",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = gameDetails.minSystemRequirements.memory ?: "?",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Storage",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = gameDetails.minSystemRequirements.storage ?: "?",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Graphics",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = gameDetails.minSystemRequirements.graphics ?: "?",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Operating System",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = gameDetails.minSystemRequirements.os ?: "?",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            Text(
                text = "Website",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = gameDetails.gameUrl,
                style = MaterialTheme.typography.caption,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onGameUrlClick(gameDetails.gameUrl) }
            )
            Text(
                text = "Copyright",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "All Rights Reserved by ${gameDetails.developer}.",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}