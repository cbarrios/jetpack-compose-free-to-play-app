package com.lalosapps.freetoplay.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.*

@ExperimentalLifecycleComposeApi
@Composable
fun SearchScreen(
    games: List<Game>,
    barTitle: String,
    onBackPress: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = games) {
        if (games != viewModel.originalList) {
            viewModel.setGames(games)
        }
    }

    val uiState = viewModel.games.collectAsStateWithLifecycle().value
    val allGenres = viewModel.allGenres.collectAsStateWithLifecycle().value
    Column(modifier = Modifier.fillMaxSize()) {
        GameDetailsNavBar(
            title = barTitle,
            onBackPress = onBackPress,
            badge = {
                Spacer(modifier = Modifier.weight(1f))
                Chip(
                    borderWidth = 1.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    borderColor = MaterialTheme.colors.primary
                ) {
                    Text(
                        text = uiState.size.toString(),
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
            },
            trailingIcon = {
                IconToggleButton(
                    checked = viewModel.showFilter,
                    onCheckedChange = viewModel::onFilterToggle
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(R.string.filter_games_content_description),
                        tint = if (viewModel.showFilter) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
                    )
                }
            }
        )
        AnimatedChipRow(
            visible = viewModel.showFilter,
            data = allGenres,
            onChipClick = viewModel::filterByGenre
        )
        SearchBar(
            query = viewModel.query,
            onQueryChange = viewModel::onQueryChange,
            onQueryClear = viewModel::onQueryClear,
            onSearch = viewModel::showSearchResults
        )
        if (!viewModel.showSearch) {
            SearchSuggestions(
                games = uiState,
                onSuggestionClick = {
                    onItemClick(it)
                }
            )
        } else {
            SearchDetails(
                games = uiState,
                onItemClick = onItemClick
            )
        }
    }
}