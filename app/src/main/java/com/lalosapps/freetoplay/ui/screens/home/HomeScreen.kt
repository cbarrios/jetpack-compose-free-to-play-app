package com.lalosapps.freetoplay.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.core.util.getRandomUrls
import com.lalosapps.freetoplay.core.util.header
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.CarouselView
import com.lalosapps.freetoplay.ui.components.GameCard

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    uiState: Resource<List<Game>>,
    onOpenDrawer: () -> Unit,
    onSearch: () -> Unit,
    onGameClick: () -> Unit
) {
    when (uiState) {
        is Resource.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error")
            }
        }
        is Resource.Success -> {
            val games = uiState.data
            if (games.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Sorry, no games available.")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        IconButton(onClick = { onOpenDrawer() }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                        IconButton(onClick = { onSearch() }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                    val randomUrls = remember { games.getRandomUrls() }
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        header {
                            CarouselView(
                                modifier = Modifier
                                    .requiredHeight(260.dp)
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                urls = randomUrls,
                                shape = MaterialTheme.shapes.medium,
                                crossFade = 1000,
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        items(games) { game ->
                            GameCard(
                                game = game,
                                onClick = { onGameClick() }
                            )
                        }
                    }
                }
            }
        }
        else -> Unit
    }
}