package com.lalosapps.freetoplay.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.ui.components.GameDetailsNavBar
import com.lalosapps.freetoplay.ui.components.SearchDetails
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.AnimatedChipRow
import com.lalosapps.freetoplay.ui.components.Chip
import com.lalosapps.freetoplay.ui.screens.base.ChipData

@ExperimentalLifecycleComposeApi
@Composable
fun FavoritesScreen(
    onBackPress: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites = viewModel.favGames.collectAsStateWithLifecycle().value
    val genres = viewModel.favGenres.collectAsStateWithLifecycle().value
    FavoritesScreen(
        favorites = favorites,
        genres = genres,
        showFilter = viewModel.showFilter,
        onFilterToggle = viewModel::onFilterToggle,
        onFilterByGenre = viewModel::filterByGenre,
        onBackPress = onBackPress,
        onItemClick = onItemClick
    )
}

@Composable
fun FavoritesScreen(
    favorites: List<Game>,
    genres: List<ChipData>,
    showFilter: Boolean,
    onFilterToggle: (Boolean) -> Unit,
    onFilterByGenre: (String) -> Unit,
    onBackPress: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GameDetailsNavBar(
            title = stringResource(id = R.string.my_games),
            onBackPress = onBackPress,
            badge = if (favorites.isNotEmpty()) {
                {
                    Spacer(modifier = Modifier.weight(1f))
                    Chip(
                        borderWidth = 1.dp,
                        backgroundColor = MaterialTheme.colors.primary,
                        borderColor = MaterialTheme.colors.primary
                    ) {
                        Text(
                            text = favorites.size.toString(),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .widthIn(max = 100.dp)
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                null
            },
            trailingIcon = if (favorites.isNotEmpty()) {
                {
                    IconToggleButton(
                        checked = showFilter,
                        onCheckedChange = onFilterToggle
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.filter_games_content_description),
                            tint = if (showFilter) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
                        )
                    }
                }
            } else {
                null
            }
        )
        if (favorites.isNotEmpty()) {
            AnimatedChipRow(
                visible = showFilter,
                data = genres,
                onChipClick = onFilterByGenre
            )
            SearchDetails(
                games = favorites,
                onItemClick = onItemClick
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.nothing_yet),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}