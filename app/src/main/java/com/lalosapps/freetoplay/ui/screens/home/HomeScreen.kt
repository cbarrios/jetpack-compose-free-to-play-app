package com.lalosapps.freetoplay.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.core.util.getGameIdFromThumbnail
import com.lalosapps.freetoplay.core.util.getRandomUrls
import com.lalosapps.freetoplay.core.util.header
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.CarouselView
import com.lalosapps.freetoplay.ui.components.GameCard
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    uiState: Resource<List<Game>>,
    games: List<Game>,
    barTitle: String,
    onOpenDrawer: () -> Unit,
    onSearch: () -> Unit,
    onGameClick: (Int) -> Unit
) {
    if (games.isEmpty()) {
        when (uiState) {
            is Resource.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_connection_error),
                        contentDescription = stringResource(R.string.ic_connection_error)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${uiState.error ?: stringResource(R.string.could_not_fetch_games)}",
                        textAlign = TextAlign.Center
                    )
                }
            }
            is Resource.Success -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.sorry_no_games_available))
                }
            }
            else -> Unit
        }
    } else {
        GamesScreen(
            games = games,
            barTitle = barTitle,
            onOpenDrawer = onOpenDrawer,
            onSearch = onSearch,
            onGameClick = onGameClick
        )
    }
}

@ExperimentalPagerApi
@Composable
fun GamesScreen(
    games: List<Game>,
    barTitle: String,
    onOpenDrawer: () -> Unit,
    onSearch: () -> Unit,
    onGameClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu_button),
                    tint = MaterialTheme.colors.onBackground
                )
            }
            Text(
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .testTag("Bar Title"),
                text = barTitle,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_button),
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }

        Box {
            val gridState = rememberLazyGridState()
            val scope = rememberCoroutineScope()
            val randomUrls = remember(games) { games.getRandomUrls() }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.testTag("Games")
            ) {
                header {
                    CarouselView(
                        modifier = Modifier
                            .requiredHeight(260.dp)
                            .fillMaxWidth()
                            .padding(8.dp),
                        urls = randomUrls,
                        shape = MaterialTheme.shapes.medium,
                        contentScale = ContentScale.FillBounds,
                        clickable = true,
                        onImageClick = { image ->
                            val gameId = games.getGameIdFromThumbnail(image)
                            gameId?.let { onGameClick(it) }
                        }
                    )
                }
                items(items = games, key = { it.id }) { game ->
                    GameCard(
                        game = game,
                        onClick = { onGameClick(game.id) }
                    )
                }
            }
            val showUpButton by remember {
                derivedStateOf {
                    gridState.firstVisibleItemIndex > 0
                }
            }
            val context = LocalContext.current
            androidx.compose.animation.AnimatedVisibility(
                visible = showUpButton,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .semantics {
                        contentDescription =
                            context.getString(R.string.scroll_up_content_description)
                    }
            ) {
                IconButton(
                    onClick = { scope.launch { gridState.scrollToItem(0) } }
                ) {
                    Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = null)
                }
            }
        }
    }
}